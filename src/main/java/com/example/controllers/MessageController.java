package com.example.controllers;

import com.example.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;

import org.springframework.stereotype.Controller;

@Controller
public class MessageController {

    @MessageMapping("/sendMessage") // Путь, по которому клиент отправляет сообщения
    @SendTo("/topic/messages") // Путь, по которому подписчики получают сообщения
    public Message sendMessage(Message message) throws Exception {
        // Можно добавить дополнительную обработку сообщений здесь
        return message;
    }
}