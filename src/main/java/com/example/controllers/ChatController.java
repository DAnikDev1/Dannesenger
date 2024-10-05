package com.example.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ChatController {

    @GetMapping("/")
    public String index() {
        return "index"; // Страница входа
    }

    @PostMapping("/chat")
    public String chat(@RequestParam String username, Model model) {
        // Валидация имени пользователя
        if (username.length() < 3 || username.length() > 20) {
            model.addAttribute("error", "Имя должно быть от 3 до 20 символов.");
            return "index";
        }
        model.addAttribute("username", username);
        return "chat"; // Страница чата
    }
}