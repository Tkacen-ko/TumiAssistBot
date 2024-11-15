package com.tkachenko.BasicTelegramBot.repository;

import com.tkachenko.BasicTelegramBot.model.UserTelegram;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserTelegram, Long> {
    Optional<UserTelegram> findByChatIdAndUserName(Long chatId, String userName);
    Optional<UserTelegram> findByChatId(Long chatId);
}