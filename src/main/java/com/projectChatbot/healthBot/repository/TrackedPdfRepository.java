package com.projectChatbot.healthBot.repository;

import com.projectChatbot.healthBot.entity.TrackedPdf;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrackedPdfRepository extends JpaRepository<TrackedPdf, Long> {

    boolean existsByUrl(String url);
}