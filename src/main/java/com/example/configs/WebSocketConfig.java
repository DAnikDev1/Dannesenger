package com.example.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.config.annotation.*;
import org.springframework.web.socket.server.HandshakeInterceptor;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final HandshakeInterceptor handshakeInterceptor;

    @Autowired
    public WebSocketConfig(HandshakeInterceptor handshakeInterceptor) {
        this.handshakeInterceptor = handshakeInterceptor;
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic"); // Endpoint для подписки клиентов
        config.setApplicationDestinationPrefixes("/app"); // Префикс для отправки сообщений с клиента
    }
    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registry) {
        registry.setMessageSizeLimit(1024 * 1024); // 512 КБ
        registry.setSendBufferSizeLimit(1024 * 1024); // 1 МБ
        registry.setSendTimeLimit(20000); // 20 секунд
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/chat") // Endpoint для подключения WebSocket
                .addInterceptors(handshakeInterceptor)
                .withSockJS(); // Поддержка SockJS для совместимости
    }
}