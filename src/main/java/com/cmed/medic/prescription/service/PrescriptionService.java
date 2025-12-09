package com.cmed.medic.prescription.service;

import java.time.LocalDate;
import java.util.Map;

import org.springframework.data.domain.Page;

import com.cmed.medic.prescription.dto.*;

public interface PrescriptionService {
    PrescriptionDTO create(PrescriptionRequest request);
    PrescriptionDTO update(Long id, PrescriptionRequest request);
    PrescriptionDTO getById(Long id);
    Page<PrescriptionDTO> getAll(int page);
    void delete(Long id);
    Map<LocalDate, Long> getPrescriptionCounts(LocalDate referenceDate);
}
