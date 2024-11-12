package com.tkachenko.BasicTelegramBot.repository;

import com.tkachenko.BasicTelegramBot.model.UserTelegram;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserTelegram, Long> {
    Optional<UserTelegram> findByChatIdAndUserName(Long chatId, String userName);
}