package com.cmed.medic.prescription.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.cmed.medic.prescription.dto.PrescriptionDTO;
import com.cmed.medic.prescription.dto.PrescriptionRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PrescriptionRepository {
        Page<PrescriptionDTO> findAllByCreatedByIdCustom(Long userId, Pageable pageable);

        Optional<PrescriptionDTO> findByIdAndCreatedByIdCustom(Long id, Long userId);

        List<Object[]> countPrescriptionsBetweenCustom(LocalDate startDate, LocalDate endDate, Long userId);

        int deleteByIdAndCreatedById(Long id);

        PrescriptionDTO save(PrescriptionRequest dto, Long userId);
        PrescriptionDTO update(Long id, PrescriptionDTO dto, Long userId);
}
