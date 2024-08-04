package com.natsukashiiz.sbws.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("messages")
public record Message(
        @Id Long id,
        AggregateReference<User, Long> senderId,
        AggregateReference<Room, Long> roomId,
        String message,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
