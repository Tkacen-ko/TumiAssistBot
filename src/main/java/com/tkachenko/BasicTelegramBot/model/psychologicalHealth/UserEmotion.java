package com.tkachenko.BasicTelegramBot.model.psychologicalHealth;

import com.tkachenko.BasicTelegramBot.model.UserTelegram;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@Table(name = "user_emotions")
public class UserEmotion {

    public UserEmotion(UserTelegram user, Emotion emotion)
    {
        this.user = user;
        this.emotion = emotion;
        createdAt = LocalDateTime.now();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private UserTelegram user; // Ссылка на пользователя

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "thought_id")
    private ThoughtUser thought; // Ссылка на мысль (может быть null)

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "emotion_id", nullable = false)
    private Emotion emotion; // Ссылка на эмоцию из справочника

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}