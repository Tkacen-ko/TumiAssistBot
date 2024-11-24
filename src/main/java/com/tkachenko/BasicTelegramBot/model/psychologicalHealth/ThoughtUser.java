package com.tkachenko.BasicTelegramBot.model.psychologicalHealth;

import com.tkachenko.BasicTelegramBot.model.UserTelegram;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Table(name = "user_though")
@NoArgsConstructor
public class ThoughtUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false, length = 3000)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY) // Связь с пользователем
    @JoinColumn(name = "user_id", nullable = false)
    private UserTelegram user;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @ElementCollection
    @CollectionTable(name = "user_thought_tags", joinColumns = @JoinColumn(name = "user_thought_id"))
    @Column(name = "tag")
    private Set<String> tags = new HashSet<>(); // Набор тегов для мыслей

    public ThoughtUser(String title) {
        this.title = title;
        this.createdAt = LocalDateTime.now();
    }
}