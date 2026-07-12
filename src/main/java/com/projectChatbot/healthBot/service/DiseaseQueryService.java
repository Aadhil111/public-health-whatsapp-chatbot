package com.projectChatbot.healthBot.service;

import com.projectChatbot.healthBot.dto.IntentResponse;
import com.projectChatbot.healthBot.repository.*;
import org.springframework.stereotype.Service;

@Service
public class DiseaseQueryService {

    private final SymptomsRepository symptomsRepo;
    private final PreventionRepository preventionRepo;
    private final VaccinationRepository vaccinationRepo;
    private final DiseaseRepository diseaseRepo;

    public DiseaseQueryService(
            SymptomsRepository symptomsRepo,
            PreventionRepository preventionRepo,
            VaccinationRepository vaccinationRepo,
            DiseaseRepository diseaseRepo) {

        this.symptomsRepo = symptomsRepo;
        this.preventionRepo = preventionRepo;
        this.vaccinationRepo = vaccinationRepo;
        this.diseaseRepo = diseaseRepo;
    }

    public String fetchContent(IntentResponse intent) {

        if (intent.getDisease() == null) {
            return null;
        }

        return switch (intent.getIntent()) {

            case "SYMPTOMS" ->
                    symptomsRepo.findByDiseaseNameIgnoreCase(intent.getDisease())
                            .map(s -> s.getSymptomsText())
                            .orElse(null);

            case "PREVENTION" ->
                    preventionRepo.findByDiseaseNameIgnoreCase(intent.getDisease())
                            .map(p -> p.getPreventionText())
                            .orElse(null);

            case "VACCINATION" ->
                    vaccinationRepo.findByDiseaseNameIgnoreCase(intent.getDisease())
                            .map(v -> v.getVaccinationText())
                            .orElse(null);

            case "DISEASE_INFO" ->
                    diseaseRepo.findByDiseaseNameIgnoreCase(intent.getDisease())
                            .map(d -> d.getDetailedDescription())
                            .orElse(null);

            default -> null;
        };
    }
}
