package by.grechishnikov.messenger.websocket;

import by.grechishnikov.messenger.common.ApplicationProperty;
import by.grechishnikov.messenger.security.service.TokenService;
import by.grechishnikov.messenger.websocket.handler.CustomHandshakeHandler;
import by.grechishnikov.messenger.websocket.handler.CustomHandshakeInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * @author - Evgeniy Grechishnikov
 */
@Configuration
@EnableWebSocketMessageBroker
public class SocketBrokerConfig implements WebSocketMessageBrokerConfigurer {

    private static final String FRONT_END_SERVER_ADDRESS =
            ApplicationProperty.getStringProperty("front-end.address");
    private TokenService tokenService;

    @Autowired
    public SocketBrokerConfig(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/chat");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/chat").setAllowedOrigins(FRONT_END_SERVER_ADDRESS)
                .setHandshakeHandler(new CustomHandshakeHandler())
                .addInterceptors(new CustomHandshakeInterceptor(tokenService)).withSockJS();
    }

}
