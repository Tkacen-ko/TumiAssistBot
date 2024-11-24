package com.tkachenko.BasicTelegramBot.repository.psychologicalHealth;

import com.tkachenko.BasicTelegramBot.model.psychologicalHealth.Emotion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmotionRepository extends JpaRepository<Emotion, Long> {
    Optional<Emotion> findByTitle(String title);
}