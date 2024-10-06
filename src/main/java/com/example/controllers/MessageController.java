package com.example.controllers;

import com.example.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

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

        return message;
    }
}
