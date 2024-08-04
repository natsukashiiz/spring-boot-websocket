package com.natsukashiiz.sbws.controller;

import com.natsukashiiz.sbws.entity.Message;
import com.natsukashiiz.sbws.entity.Room;
import com.natsukashiiz.sbws.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @GetMapping
    public List<Room> getAllRooms() {
        return roomService.getAllRooms();
    }

    @GetMapping("/{roomId}/messages")
    public List<Message> getMessagesByRoomId(@PathVariable Long roomId) {
        return roomService.getMessagesByRoomId(roomId);
    }
}
