package com.tkachenko.BasicTelegramBot.repository.psychologicalHealth;

import com.tkachenko.BasicTelegramBot.model.psychologicalHealth.ThoughtUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ThoughtUserRepository  extends JpaRepository<ThoughtUser, Long> {
}
