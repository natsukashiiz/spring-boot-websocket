package com.natsukashiiz.sbws.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final JwtEncoder encoder;
    private final JwtDecoder decoder;

    public String generateToken(Long userId) {
        Instant now = Instant.now();
        JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
                .issuer("spring-boot-websocket")
                .issuedAt(now)
                .expiresAt(now.plus(Duration.ofDays(365)))
                .id(UUID.randomUUID().toString())
                .subject(String.valueOf(userId))
                .build();
        return this.encoder.encode(JwtEncoderParameters.from(jwtClaimsSet)).getTokenValue();
    }

    public Jwt decode(String token) {
        return this.decoder.decode(token);
    }

    public boolean validateToken(String token) {
        try {
            this.decoder.decode(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
