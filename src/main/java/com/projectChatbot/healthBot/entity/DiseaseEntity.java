package com.projectChatbot.healthBot.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "disease")
public class DiseaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "disease_name", unique = true)
    private String diseaseName;

    @Column(columnDefinition = "TEXT")
    private String shortDescription;

    @Column(columnDefinition = "LONGTEXT")
    private String detailedDescription;

    private String diseaseType;
    private String transmissionMethod;
    private String incubationPeriod;

    @Column(columnDefinition = "LONGTEXT")
    private String riskFactors;

    @Column(columnDefinition = "LONGTEXT")
    private String complications;

    @Column(columnDefinition = "LONGTEXT")
    private String whenToSeeDoctor;

    public String getDetailedDescription() {
        return detailedDescription;
    }
}
