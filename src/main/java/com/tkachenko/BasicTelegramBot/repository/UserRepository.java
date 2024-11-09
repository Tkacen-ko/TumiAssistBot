package com.tkachenko.BasicTelegramBot.repository;

import com.tkachenko.BasicTelegramBot.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}