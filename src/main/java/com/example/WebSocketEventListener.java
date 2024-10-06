package com.example;

import com.example.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class WebSocketEventListener {
    private final SimpMessagingTemplate messagingTemplate;
    private final ConcurrentHashMap<String, String> sessionIdUsernameMap = new ConcurrentHashMap<>();

    public WebSocketEventListener(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @EventListener
    public void handleSessionConnected(SessionConnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = headerAccessor.getSessionId();
        String username = (String) headerAccessor.getSessionAttributes().get("username");

        if (username != null) {
            sessionIdUsernameMap.put(sessionId, username);

            // Отправляем уведомление о новом пользователе
            Message message = new Message();
            message.setFrom("System");
            message.setContent("Вошёл новый пользователь с ником " + username);
            message.setTimestamp(LocalDateTime.now());
            messagingTemplate.convertAndSend("/topic/messages", message);
        }
    }
    @EventListener
    public void handleSessionDisconnect(SessionDisconnectEvent event) {
        String sessionId = event.getSessionId();
        String username = sessionIdUsernameMap.remove(sessionId);
        if (username != null) {
            UserService userService = new UserService();
            userService.removeUser(username);

            Message message = new Message();
            message.setFrom("Система");
            message.setContent("Пользователь " + username + " покинул чат");
            messagingTemplate.convertAndSend("/topic/messages", message);
        }
    }
}
