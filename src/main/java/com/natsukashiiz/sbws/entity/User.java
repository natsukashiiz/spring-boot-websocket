package com.natsukashiiz.sbws.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.List;

@Table("users")
public record User(
        @Id Long id,
        String name,
        @MappedCollection(idColumn = "sender_id", keyColumn = "sender_id") List<Message> messages,
        @MappedCollection(idColumn = "user_id", keyColumn = "user_id") List<RoomUser> rooms,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
