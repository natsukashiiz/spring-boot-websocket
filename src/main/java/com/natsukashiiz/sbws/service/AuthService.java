package com.natsukashiiz.sbws.service;

import com.natsukashiiz.sbws.entity.User;
import com.natsukashiiz.sbws.exception.AuthException;
import com.natsukashiiz.sbws.exception.BaseException;
import com.natsukashiiz.sbws.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
@Log4j2
@RequiredArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;


    public User getUser() throws BaseException {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            log.warn("GetUser-[block]:(authentication is null)");
            throw AuthException.unauthorized();
        }

        if (!authentication.isAuthenticated()) {
            log.warn("GetUser-[block]:(not authenticated)");
            throw AuthException.unauthorized();
        }

        var jwt = (Jwt) authentication.getCredentials();

        if (jwt == null) {
            log.warn("GetUser-[block]:(jwt is null)");
            throw AuthException.unauthorized();
        }

        var accountId = authentication.getName();

        if (ObjectUtils.isEmpty(accountId)) {
            log.warn("GetUser-[block]:(accountId is empty)");
            throw AuthException.unauthorized();
        }

        var accountOptional = userRepository.findById(Long.parseLong(accountId));
        if (accountOptional.isEmpty()) {
            log.warn("GetUser-[block]:(not found account). accountId:{}", accountId);
            throw AuthException.unauthorized();
        }

        return accountOptional.get();
    }

    public User getUserPrincipal() throws BaseException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            log.warn("GetUserPrincipal-[block]: Authentication is null or not authenticated");
            throw AuthException.unauthorized();
        }

        String accountId = (String) authentication.getPrincipal();

        if (ObjectUtils.isEmpty(accountId)) {
            log.warn("GetUserPrincipal-[block]: Account ID is empty");
            throw AuthException.unauthorized();
        }

        return userRepository.findById(Long.parseLong(accountId))
                .orElseThrow(() -> {
                    log.warn("GetUserPrincipal-[block]: Account not found. Account ID: {}", accountId);
                    return AuthException.unauthorized();
                });
    }

    public boolean anonymous() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        return ObjectUtils.isEmpty(authentication) || authentication.getPrincipal().equals("anonymousUser");
    }

    public boolean passwordNotMatch(String raw, String hash) {
        return !passwordEncoder.matches(raw, hash);
    }
}
