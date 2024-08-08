package com.natsukashiiz.sbws.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("room_users")
public record RoomUser(
        @Id Long id,
        AggregateReference<User, Long> userId,
        AggregateReference<Room, Long> roomId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
