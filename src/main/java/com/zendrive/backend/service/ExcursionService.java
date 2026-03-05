package com.zendrive.backend.service;

import com.zendrive.backend.dto.CreateExcursionRequest;
import com.zendrive.backend.dto.ExcursionDTO;
import com.zendrive.backend.dto.ExcursionStepDTO;
import com.zendrive.backend.entity.Excursion;
import com.zendrive.backend.entity.ExcursionStep;
import com.zendrive.backend.mapper.ExcursionMapper;
import com.zendrive.backend.repository.ExcursionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExcursionService {

    private final ExcursionRepository excursionRepository;
    private final ExcursionMapper excursionMapper;

    public List<ExcursionDTO> getActiveExcursions() {
        return excursionRepository.findByActiveTrue().stream()
                .map(excursionMapper::toDTO).toList();
    }

    public Page<ExcursionDTO> getAllExcursions(Pageable pageable) {
        return excursionRepository.findAll(pageable).map(excursionMapper::toDTO);
    }

    public ExcursionDTO getExcursionById(Long id) {
        Excursion excursion = excursionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Excursion not found"));
        return excursionMapper.toDTO(excursion);
    }

    @Transactional
    public ExcursionDTO createExcursion(CreateExcursionRequest request) {
        Excursion excursion = excursionMapper.toEntity(request);
        excursion.setActive(true);

        if (request.getSteps() != null) {
            List<ExcursionStep> steps = new ArrayList<>();
            for (ExcursionStepDTO stepDTO : request.getSteps()) {
                ExcursionStep step = ExcursionStep.builder()
                        .excursion(excursion)
                        .stepOrder(stepDTO.getStepOrder())
                        .locationName(stepDTO.getLocationName())
                        .latitude(stepDTO.getLatitude())
                        .longitude(stepDTO.getLongitude())
                        .description(stepDTO.getDescription())
                        .durationMinutes(stepDTO.getDurationMinutes())
                        .imageUrl(stepDTO.getImageUrl())
                        .build();
                steps.add(step);
            }
            excursion.setSteps(steps);
        }

        return excursionMapper.toDTO(excursionRepository.save(excursion));
    }

    @Transactional
    public ExcursionDTO updateExcursion(Long id, CreateExcursionRequest request) {
        Excursion excursion = excursionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Excursion not found"));

        excursionMapper.updateEntity(request, excursion);

        excursion.getSteps().clear();
        if (request.getSteps() != null) {
            for (ExcursionStepDTO stepDTO : request.getSteps()) {
                ExcursionStep step = ExcursionStep.builder()
                        .excursion(excursion)
                        .stepOrder(stepDTO.getStepOrder())
                        .locationName(stepDTO.getLocationName())
                        .latitude(stepDTO.getLatitude())
                        .longitude(stepDTO.getLongitude())
                        .description(stepDTO.getDescription())
                        .durationMinutes(stepDTO.getDurationMinutes())
                        .imageUrl(stepDTO.getImageUrl())
                        .build();
                excursion.getSteps().add(step);
            }
        }

        return excursionMapper.toDTO(excursionRepository.save(excursion));
    }

    @Transactional
    public void deactivateExcursion(Long id) {
        Excursion excursion = excursionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Excursion not found"));
        excursion.setActive(false);
        excursionRepository.save(excursion);
    }

    public long countActive() {
        return excursionRepository.countByActiveTrue();
    }
}
