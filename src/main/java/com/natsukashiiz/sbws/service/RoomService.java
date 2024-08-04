package com.natsukashiiz.sbws.service;

import com.natsukashiiz.sbws.entity.Message;
import com.natsukashiiz.sbws.entity.Room;
import com.natsukashiiz.sbws.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;

    public List<Room> getAllRooms() {
        return roomRepository.findAllByOrderByCreatedAtDesc();
    }

    public List<Message> getMessagesByRoomId(Long roomId) {
        return roomRepository.findById(roomId)
                .map(Room::messages)
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));
    }
}
