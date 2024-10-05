package com.example.controllers;

import com.example.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ChatController {

    private final UserService userService;

    public ChatController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String index() {
        return "index"; // Страница входа
    }

    @PostMapping("/chat")
    public String chat(@RequestParam String username, Model model, HttpSession session) {
        // Валидация имени пользователя
        if (username.length() < 3 || username.length() > 20) {
            model.addAttribute("error", "Имя должно быть от 3 до 20 символов.");
            return "index";
        }
        // Проверка на уникальность никнейма
        if (!userService.addUser(username)) {
            model.addAttribute("error", "Имя пользователя уже занято. Пожалуйста, выберите другое.");
            return "index";
        }
        session.setAttribute("username", username);
        model.addAttribute("username", username);
        return "chat"; // Страница чата
    }
}