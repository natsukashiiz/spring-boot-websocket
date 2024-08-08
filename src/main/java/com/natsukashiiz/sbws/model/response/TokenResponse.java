package com.natsukashiiz.sbws.model.response;

import com.natsukashiiz.sbws.entity.User;

public record TokenResponse(
        String token,
        User user
) {
}
