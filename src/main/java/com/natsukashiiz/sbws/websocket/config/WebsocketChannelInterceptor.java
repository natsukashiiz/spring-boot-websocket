package com.natsukashiiz.sbws.websocket.config;

import com.natsukashiiz.sbws.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class WebsocketChannelInterceptor implements ChannelInterceptor {

    private final TokenService tokenService;

    @Override
    public Message<?> preSend(@NonNull Message<?> message, @NonNull MessageChannel channel) {
        final var accessor = readHeaderAccessor(message);

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            var token = readAuthenticationHeader(accessor);
            if (!token.startsWith("Bearer ")) {
                System.out.println("No valid token found in headers");
                throw new AuthenticationCredentialsNotFoundException("No valid token found in headers");
            }

            token = token.replace("Bearer ", "");
            if (!tokenService.validateToken(token)) {
                System.out.println("Token validation failed");
                throw new AuthenticationCredentialsNotFoundException("Token validation failed");
            }

            var jwt = tokenService.decode(token);
            var user = new UsernamePasswordAuthenticationToken(jwt.getSubject(), null);
            accessor.setUser(user);
            accessor.setHeader("connection-time", LocalDateTime.now().toString());
            System.out.println("Authentication successful for user: " + user);
        }
        return message;
    }

    private StompHeaderAccessor readHeaderAccessor(Message<?> message) {
        final StompHeaderAccessor accessor = getAccessor(message);
        if (accessor == null) {
            throw new AuthenticationCredentialsNotFoundException("Fail to read headers.");
        }
        return accessor;
    }

    private String readAuthenticationHeader(StompHeaderAccessor accessor) {
        final String authKey = accessor.getFirstNativeHeader(HttpHeaders.AUTHORIZATION);
        if (authKey == null || authKey.trim().isEmpty()) {
            throw new AuthenticationCredentialsNotFoundException("Authentication header not found.");
        }
        return authKey;
    }

    StompHeaderAccessor getAccessor(Message<?> message) {
        return MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
    }
}