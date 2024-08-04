package com.natsukashiiz.sbws.repository;

import com.natsukashiiz.sbws.entity.Message;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends CrudRepository<Message, Long> {
}
