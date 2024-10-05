package com.example;

import com.example.services.UserService;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class WebSocketEventListener {

    private final UserService userService;
    private final ConcurrentHashMap<String, String> sessionIdUsernameMap = new ConcurrentHashMap<>();

    public WebSocketEventListener(UserService userService) {
        this.userService = userService;
    }

    @EventListener
    public void handleSessionConnect(org.springframework.web.socket.messaging.SessionConnectedEvent event) {
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.wrap(event.getMessage());
        String sessionId = headerAccessor.getSessionId();
        String username = (String) headerAccessor.getSessionAttributes().get("username");
        if (username != null) {
            sessionIdUsernameMap.put(sessionId, username);
        }
    }

    @EventListener
    public void handleSessionDisconnect(SessionDisconnectEvent event) {
        String sessionId = event.getSessionId();
        String username = sessionIdUsernameMap.remove(sessionId);
        if (username != null) {
            userService.removeUser(username);
        }
    }
}