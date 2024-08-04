package com.natsukashiiz.sbws.service;

import com.natsukashiiz.sbws.entity.User;
import com.natsukashiiz.sbws.model.LoginRequest;
import com.natsukashiiz.sbws.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User login(LoginRequest request) {
        var userOptional = userRepository.findByName(request.name());
        if (userOptional.isEmpty()) {
            var user = new User(null, request.name(), null, null, null);
            return userRepository.save(user);
        }

        return userOptional.get();
    }
}
