package com.natsukashiiz.sbws.repository;

import com.natsukashiiz.sbws.entity.Room;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends CrudRepository<Room, Long> {
    List<Room> findAllByOrderByCreatedAtDesc();
    Optional<Room> findByName(String name);
}
