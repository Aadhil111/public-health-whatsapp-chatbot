package com.projectChatbot.healthBot.repository;

import com.projectChatbot.healthBot.entity.PreventionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PreventionRepository
        extends JpaRepository<PreventionEntity, Long> {

    Optional<PreventionEntity> findByDiseaseNameIgnoreCase(String diseaseName);
}
