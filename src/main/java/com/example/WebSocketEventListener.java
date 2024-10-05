package com.example;

import com.example.services.UserService;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class WebSocketEventListener {

    private final UserService userService;
    private final SimpMessagingTemplate messagingTemplate;
    private final ConcurrentHashMap<String, String> sessionIdUsernameMap = new ConcurrentHashMap<>();

    public WebSocketEventListener(UserService userService, SimpMessagingTemplate messagingTemplate) {
        this.userService = userService;
        this.messagingTemplate = messagingTemplate;
    }

    @EventListener
    public void handleSessionDisconnect(SessionDisconnectEvent event) {
        String sessionId = event.getSessionId();
        String username = sessionIdUsernameMap.remove(sessionId);
        if (username != null) {
            userService.removeUser(username);
        }
    }
    @EventListener
    public void handleSessionConnect(SessionConnectedEvent event) {
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.wrap(event.getMessage());
        String sessionId = headerAccessor.getSessionId();
        Map<String, Object> sessionAttributes = headerAccessor.getSessionAttributes();

        if (sessionAttributes != null) {
            String username = (String) sessionAttributes.get("username");
            if (username != null) {
                sessionIdUsernameMap.put(sessionId, username);

                // Отправляем уведомление о новом пользователе
                Message message = new Message();
                message.setFrom("Система");
                message.setContent("Вошёл новый пользователь с ником " + username);
                messagingTemplate.convertAndSend("/topic/messages", message);
            }
        }
    }
}