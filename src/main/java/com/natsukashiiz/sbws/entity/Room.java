package com.natsukashiiz.sbws.entity;

import com.natsukashiiz.sbws.common.RoomType;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.List;

@Table("rooms")
public record Room(
        @Id Long id,
        String name,
        RoomType type,
        @MappedCollection(idColumn = "room_id", keyColumn = "room_id") List<Message> messages,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
