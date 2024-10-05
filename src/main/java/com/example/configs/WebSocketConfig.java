package com.example.configs;

import com.example.HttpHandshakeInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic"); // Endpoint для подписки клиентов
        config.setApplicationDestinationPrefixes("/app"); // Префикс для отправки сообщений с клиента
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/chat") // Endpoint для подключения WebSocket
                .addInterceptors(new HttpHandshakeInterceptor())
                .withSockJS(); // Поддержка SockJS для совместимости
    }
}