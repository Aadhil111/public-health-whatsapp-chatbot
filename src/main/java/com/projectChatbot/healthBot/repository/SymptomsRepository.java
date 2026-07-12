package com.projectChatbot.healthBot.repository;

import com.projectChatbot.healthBot.entity.SymptomsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SymptomsRepository
        extends JpaRepository<SymptomsEntity, Long> {

    Optional<SymptomsEntity> findByDiseaseNameIgnoreCase(String diseaseName);
}
