package com.natsukashiiz.sbws.repository;

import com.natsukashiiz.sbws.common.RoomType;
import com.natsukashiiz.sbws.entity.RoomUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoomUserRepository extends CrudRepository<RoomUser, Long> {
    Optional<RoomUser> findByRoomIdAndUserId(Long roomId, Long userId);
}
