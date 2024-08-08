package com.natsukashiiz.sbws.repository;

import com.natsukashiiz.sbws.entity.Room;
import com.natsukashiiz.sbws.entity.RoomUser;
import com.natsukashiiz.sbws.entity.User;
import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoomUserRepository extends CrudRepository<RoomUser, Long> {
    Optional<RoomUser> findByRoomIdAndUserId(AggregateReference<Room, Long> roomId, AggregateReference<User, Long> userId);
    boolean existsByRoomIdAndUserId(AggregateReference<Room, Long> roomId, AggregateReference<User, Long> userId);
}
