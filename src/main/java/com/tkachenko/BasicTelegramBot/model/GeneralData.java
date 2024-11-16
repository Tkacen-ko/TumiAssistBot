package com.tkachenko.BasicTelegramBot.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@MappedSuperclass
public class GeneralData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;
}