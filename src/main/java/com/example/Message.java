package com.example;

import java.time.LocalDateTime;

public class Message {
    private String from;
    private String content;
    private LocalDateTime timestamp;
    private String image; // Новое поле для изображения

    // Конструкторы
    public Message() {
        this.timestamp = LocalDateTime.now();
    }

    public Message(String from, String content) {
        this.from = from;
        this.content = content;
        this.timestamp = LocalDateTime.now();
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}