package com.natsukashiiz.sbws.model.request;

public record SendMessageRequest(
        Long senderId,
        Long roomId,
        String message
) {
}
