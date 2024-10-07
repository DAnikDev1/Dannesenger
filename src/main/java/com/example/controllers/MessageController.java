package com.example.controllers;

import com.example.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Controller
public class MessageController {

    @MessageMapping("/sendMessage")
    @SendTo("/topic/messages")
    public Message sendMessage(Message message) {
        String content = message.getContent();
        String image = message.getImage();

        if (content != null && content.length() > 600) {
            message.setContent(content.substring(0, 600));
        }

        if (image != null) {
        }
        message.setTimestamp(LocalDateTime.now(ZoneId.of("Asia/Yekaterinburg")));

        return message;
    }
}
