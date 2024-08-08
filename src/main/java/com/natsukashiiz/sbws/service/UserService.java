package com.natsukashiiz.sbws.service;

import com.natsukashiiz.sbws.entity.User;
import com.natsukashiiz.sbws.model.request.LoginRequest;
import com.natsukashiiz.sbws.model.response.TokenResponse;
import com.natsukashiiz.sbws.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final TokenService tokenService;

    public TokenResponse login(LoginRequest request) {
        var userOptional = userRepository.findByName(request.name());
        if (userOptional.isEmpty()) {
            var user = new User(null, request.name(), null, null, null, null);
            return createTokenResponse(userRepository.save(user));
        }

        return createTokenResponse(userOptional.get());
    }

    private TokenResponse createTokenResponse(User user) {
        var token = tokenService.generateToken(user.id());
        return new TokenResponse(token, user);
    }
}
