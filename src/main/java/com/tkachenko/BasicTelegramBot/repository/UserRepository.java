package com.tkachenko.BasicTelegramBot.repository;

import com.tkachenko.BasicTelegramBot.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByChatIdAndUserName(Long chatId, String userName);
}