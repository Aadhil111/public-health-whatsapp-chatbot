package com.projectChatbot.healthBot.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "prevention")
public class PreventionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "disease_name", unique = true)
    private String diseaseName;

    @Column(name = "prevention_text", columnDefinition = "LONGTEXT")
    private String preventionText;

    public String getDiseaseName() {
        return diseaseName;
    }

    public String getPreventionText() {
        return preventionText;
    }
}
