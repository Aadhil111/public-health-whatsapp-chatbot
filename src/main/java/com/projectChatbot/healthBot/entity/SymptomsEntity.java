package com.projectChatbot.healthBot.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "symptoms")
public class SymptomsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "disease_name", unique = true)
    private String diseaseName;

    @Column(name = "symptoms_text", columnDefinition = "LONGTEXT")
    private String symptomsText;

    public String getDiseaseName() {
        return diseaseName;
    }

    public String getSymptomsText() {
        return symptomsText;
    }
}
