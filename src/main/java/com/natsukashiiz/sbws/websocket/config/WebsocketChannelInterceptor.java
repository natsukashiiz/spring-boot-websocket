package com.natsukashiiz.sbws.websocket.config;

import com.natsukashiiz.sbws.repository.RoomUserRepository;
import com.natsukashiiz.sbws.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jdbc.core.mapping.AggregateReference;
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
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class WebsocketChannelInterceptor implements ChannelInterceptor {

    private final TokenService tokenService;
    private final RoomUserRepository roomUserRepository;

    @Override
    public Message<?> preSend(@NonNull Message<?> message, @NonNull MessageChannel channel) {
        final var accessor = readHeaderAccessor(message);

        System.out.println("Accessor: " + accessor);
        System.out.println("Command: " + accessor.getCommand());
        System.out.println("Destination: " + accessor.getDestination());

        if (StompCommand.CONNECT.equals(accessor.getCommand()) || StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {

            // ignore destination /topic/errors
            if (accessor.getDestination() != null && accessor.getDestination().equals("/topic/errors")) {
                return message;
            }

            // ignore server send messages
            if (accessor.getDestination() != null && accessor.getDestination().startsWith("/user")) {
                return message;
            }

            String userId;
            if (accessor.getUser() == null) {
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

                userId = jwt.getSubject();
            } else {
                userId = accessor.getUser().getName();
            }

            if (StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
                var roomId = Objects.requireNonNull(accessor.getDestination()).replace("/topic/rooms/", "");

                if (!roomUserRepository.existsByRoomIdAndUserId(AggregateReference.to(Long.parseLong(roomId)), AggregateReference.to(Long.parseLong(userId)))) {
                    System.out.println("User is not a member of the room");
                    return null;
                }
            }
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