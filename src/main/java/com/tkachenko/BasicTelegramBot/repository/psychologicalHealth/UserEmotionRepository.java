package com.tkachenko.BasicTelegramBot.repository.psychologicalHealth;

import com.tkachenko.BasicTelegramBot.model.psychologicalHealth.UserEmotion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserEmotionRepository extends JpaRepository<UserEmotion, Long> {
}