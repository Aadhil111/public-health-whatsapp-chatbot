package com.projectChatbot.healthBot.repository;

import com.projectChatbot.healthBot.entity.DiseaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DiseaseRepository
        extends JpaRepository<DiseaseEntity, Long> {

    Optional<DiseaseEntity> findByDiseaseNameIgnoreCase(String diseaseName);
}
