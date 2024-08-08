package com.natsukashiiz.sbws.controller;

import com.natsukashiiz.sbws.entity.Message;
import com.natsukashiiz.sbws.entity.Room;
import com.natsukashiiz.sbws.exception.BaseException;
import com.natsukashiiz.sbws.model.request.SendMessageRequest;
import com.natsukashiiz.sbws.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @GetMapping
    public Iterable<Room> getAllRooms() throws BaseException {
        return roomService.getMyRooms();
    }

    @GetMapping("/{roomId}/messages")
    public List<Message> getMessagesByRoomId(@PathVariable Long roomId) throws BaseException {
        return roomService.getMessagesByRoomId(roomId);
    }

    @PostMapping("/{roomId}/messages")
    public Message sendMessage(@PathVariable Long roomId, @RequestBody SendMessageRequest request) throws BaseException {
        return roomService.sendMessage(roomId, request);
    }
}
