package com.tkachenko.BasicTelegramBot.repository;

import com.tkachenko.BasicTelegramBot.model.MessageTG;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<MessageTG, Long> {
    List<MessageTG> findTop10ByOrderByTimestampDesc();
}