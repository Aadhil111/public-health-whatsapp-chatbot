package com.projectChatbot.healthBot.repository;

import com.projectChatbot.healthBot.entity.VaccinationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VaccinationRepository
        extends JpaRepository<VaccinationEntity, Long> {

    Optional<VaccinationEntity> findByDiseaseNameIgnoreCase(String diseaseName);
}
