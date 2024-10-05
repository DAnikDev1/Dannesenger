package com.example.controllers;

import com.example.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.concurrent.ConcurrentHashMap;

@Controller
public class MessageController {

    private final ConcurrentHashMap<String, Long> userLastMessageTime = new ConcurrentHashMap<>();
    private static final long MESSAGE_COOLDOWN_MILLIS = 200; // 0.2 секунды

    @MessageMapping("/sendMessage")
    @SendTo("/topic/messages")
    public Message sendMessage(Message message) {
        String username = message.getFrom();
        long currentTime = System.currentTimeMillis();

        Long lastMessageTime = userLastMessageTime.get(username);

        if (lastMessageTime != null && (currentTime - lastMessageTime) < MESSAGE_COOLDOWN_MILLIS) {
            // Если сообщение отправлено слишком быстро, игнорируем его
            return null;
        }

        userLastMessageTime.put(username, currentTime);

        // Проверка длины сообщения
        if (message.getContent().length() > 600) {
            message.setContent(message.getContent().substring(0, 600));
        }

        return message;
    }
}