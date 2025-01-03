package com.example.linkup.repositories;

import com.example.linkup.models.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    Page<Message> findAllByChatRoomId(Long chatRoomId, Pageable pageable);
}
