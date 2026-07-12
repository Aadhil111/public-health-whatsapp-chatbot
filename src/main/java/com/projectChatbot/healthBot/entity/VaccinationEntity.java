package com.projectChatbot.healthBot.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "vaccination")
public class VaccinationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "disease_name", unique = true)
    private String diseaseName;

    @Column(name = "vaccination_text", columnDefinition = "LONGTEXT")
    private String vaccinationText;

    public String getDiseaseName() {
        return diseaseName;
    }

    public String getVaccinationText() {
        return vaccinationText;
    }
}
