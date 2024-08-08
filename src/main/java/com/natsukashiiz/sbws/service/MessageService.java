package com.natsukashiiz.sbws.service;

import com.natsukashiiz.sbws.common.RoomType;
import com.natsukashiiz.sbws.entity.Message;
import com.natsukashiiz.sbws.exception.BaseException;
import com.natsukashiiz.sbws.exception.RoomException;
import com.natsukashiiz.sbws.model.request.SendMessageRequest;
import com.natsukashiiz.sbws.repository.MessageRepository;
import com.natsukashiiz.sbws.repository.RoomUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final RoomUserRepository roomUserRepository;
    private final SimpMessagingTemplate template;

    public Message sendMessage(SendMessageRequest request) throws BaseException {

        // validate if the sender is a member of the room
        var roomUser = roomUserRepository.findByRoomIdAndUserId(request.roomId(), request.senderId());
        if (roomUser.isEmpty()) {
            throw RoomException.userNotInRoom();
        }

        var message = new Message(
                null,
                AggregateReference.to(request.senderId()),
                AggregateReference.to(request.roomId()),
                request.message(),
                null,
                null);

        var destination = String.format("/topic/rooms/%d", request.roomId());
        template.convertAndSend(destination, message);

        return messageRepository.save(message);
    }
}
