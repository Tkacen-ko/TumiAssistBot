package com.tkachenko.BasicTelegramBot.model.psychologicalHealth;

import com.tkachenko.BasicTelegramBot.model.GeneralData;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "emotions")
public class Emotion extends GeneralData {
    public Emotion(Long id, String title) {
        super(id, title);
    }
}