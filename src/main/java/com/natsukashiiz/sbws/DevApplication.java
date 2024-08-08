package com.natsukashiiz.sbws;

import com.natsukashiiz.sbws.common.RoomType;
import com.natsukashiiz.sbws.entity.Room;
import com.natsukashiiz.sbws.entity.User;
import com.natsukashiiz.sbws.repository.RoomRepository;
import com.natsukashiiz.sbws.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Log4j2
public class DevApplication implements ApplicationRunner {

    private final UserRepository userRepository;
    private final RoomRepository roomRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.debug("DevApplication is running");

        var userCount = userRepository.count();
        if (userCount == 0) {
            userRepository.saveAll(List.of(
                    new User(null, "user1", null, null, null),
                    new User(null, "user2", null, null, null)
            ));
        }

        var roomCount = roomRepository.count();
        if (roomCount == 0) {
            roomRepository.saveAll(List.of(
                    new Room(null, "room-friend", RoomType.Friend, null, null, null),
                    new Room(null, "room-group", RoomType.Group, null, null, null)
            ));
        }
    }
}
