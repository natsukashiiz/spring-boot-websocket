package com.natsukashiiz.sbws.controller.ws;

import com.natsukashiiz.sbws.entity.Message;
import com.natsukashiiz.sbws.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class WsRoomController {

    private final RoomService roomService;

    @SubscribeMapping("/rooms/{roomId}")
    public List<Message> handleSubscription(@DestinationVariable Long roomId) {
        return roomService.getMessagesByRoomId(roomId);
    }
}
