package com.natsukashiiz.sbws.model;

public record SendMessageRequest(
        Long senderId,
        Long roomId,
        String message
) {
}
