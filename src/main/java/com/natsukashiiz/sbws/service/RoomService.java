package com.natsukashiiz.sbws.service;

import com.natsukashiiz.sbws.entity.Message;
import com.natsukashiiz.sbws.entity.Room;
import com.natsukashiiz.sbws.entity.RoomUser;
import com.natsukashiiz.sbws.exception.BaseException;
import com.natsukashiiz.sbws.exception.RoomException;
import com.natsukashiiz.sbws.model.request.SendMessageRequest;
import com.natsukashiiz.sbws.repository.MessageRepository;
import com.natsukashiiz.sbws.repository.RoomRepository;
import com.natsukashiiz.sbws.repository.RoomUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final MessageRepository messageRepository;
    private final RoomUserRepository roomUserRepository;
    private final SimpMessagingTemplate template;
    private final AuthService authService;

    public List<Room> getAllRooms() {
        return roomRepository.findAllByOrderByCreatedAtDesc();
    }

    public Iterable<Room> getMyRooms() throws BaseException {
        var user = authService.getUser();
        List<Long> roomIds = user.rooms()
                .stream()
                .map(RoomUser::roomId)
                .map(AggregateReference::getId)
                .toList();
        return roomRepository.findAllById(roomIds);
    }

    public List<Message> getMessagesByRoomId(Long roomId) throws BaseException {
        var user = authService.getUser();

        // validate if the sender is a member of the room
        if (!checkPermission(roomId, user.id())) {
            throw RoomException.notFound();
        }

        return roomRepository.findById(roomId)
                .map(Room::messages)
                .orElseThrow(RoomException::notFound);
    }

    public boolean checkPermission(Long roomId, Long userId) throws BaseException {
        return roomUserRepository.findByRoomIdAndUserId(
                AggregateReference.to(roomId),
                AggregateReference.to(userId)
        ).isPresent();
    }

    public Message sendMessage(Long roomId, SendMessageRequest request) throws BaseException {
        var user = authService.getUser();

        // validate if the sender is a member of the room
        if (!checkPermission(roomId, user.id())) {
            throw RoomException.notFound();
        }

        var message = new Message(
                null,
                AggregateReference.to(user.id()),
                AggregateReference.to(roomId),
                request.message(),
                LocalDateTime.now(),
                LocalDateTime.now());

        var destination = String.format("/topic/rooms/%d", roomId);
        template.convertAndSend(destination, message);

        return messageRepository.save(message);
    }
}
