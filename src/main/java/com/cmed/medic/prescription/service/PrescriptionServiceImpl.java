package com.cmed.medic.prescription.service;

import com.cmed.medic.auth.dto.UserDTO;
import com.cmed.medic.auth.dto.UserPrincipal;
import com.cmed.medic.auth.repository.UserRepository;
import com.cmed.medic.prescription.dto.*;
import com.cmed.medic.prescription.repository.PrescriptionRepository;
import com.cmed.medic.prescription.validation.PrescriptionValidation;
import com.cmed.medic.utils.ValidationResult;
import com.cmed.medic.utils.CustomExceptions.InvalidInputException;
import com.cmed.medic.utils.CustomExceptions.ResourceNotFoundException;

import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class PrescriptionServiceImpl implements PrescriptionService {

    private final PrescriptionRepository repo;
    private final UserRepository repoUser;

    public PrescriptionServiceImpl(PrescriptionRepository repo, UserRepository repoUser) {
        this.repo = repo;
        this.repoUser = repoUser;
    }

    private Long getCurrentUserId() {
        UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return principal.getId();
    }

    @Override
    public PrescriptionDTO create(PrescriptionRequest r) {
        ValidationResult validation = PrescriptionValidation.validatePrescriptionRequest(r);
        if (!validation.isValid()) {
            throw new InvalidInputException(validation.getMessage());
        }

        Long userId = getCurrentUserId();

        Optional<UserDTO> user = repoUser.findById(userId);
        user.orElseThrow(() -> new ResourceNotFoundException("User does not exist"));

        return repo.save(r, userId);
    }

    @Override
    public PrescriptionDTO update(Long id, PrescriptionRequest r) {
        ValidationResult prescriptionValidation = PrescriptionValidation.validatePrescriptionUpdateRequest(r);
        if (!prescriptionValidation.isValid()) {
            throw new InvalidInputException(prescriptionValidation.getMessage());
        }

        Long userId = getCurrentUserId();
        PrescriptionDTO p = repo.findByIdAndCreatedByIdCustom(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Prescription not found"));

        if (r.patientName() != null) {
            p.setPatientName(r.patientName());
        }
        if (r.patientAge() != null) {
            p.setPatientAge(r.patientAge());
        }
        if (r.patientGender() != null) {
            p.setPatientGender(r.patientGender());
        }
        if (r.diagnosis() != null) {
            p.setDiagnosis(r.diagnosis());
        }
        if (r.medicines() != null) {
            p.setMedicines(r.medicines());
        }
        if (r.nextVisitDate() != null) {
            p.setNextVisitDate(LocalDate.parse(r.nextVisitDate()));
        }

        return repo.update(id, p, userId);
    }

    @Override
    public PrescriptionDTO getById(Long id) {
        Long userId = getCurrentUserId();

        return repo.findByIdAndCreatedByIdCustom(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Prescription not found"));
    }

    @Override
    public Page<PrescriptionDTO> getAll(int page) {
        Long userId = getCurrentUserId();
        Pageable pageable = PageRequest.of(page, 10);
        return repo.findAllByCreatedByIdCustom(userId, pageable);
    }

    @Override
    public void delete(Long id) {
        Long userId = getCurrentUserId();

        Optional<PrescriptionDTO> p = repo.findByIdAndCreatedByIdCustom(id, userId);
        p.orElseThrow(() -> new ResourceNotFoundException("Prescription not found"));

        repo.deleteByIdAndCreatedById(id);
    }

    @Override
    public Map<LocalDate, Long> getPrescriptionCounts(LocalDate referenceDate) {
        if (referenceDate == null) {
            referenceDate = LocalDate.now();
        }

        LocalDate startDate = referenceDate.minusDays(9);
        LocalDate endDate = referenceDate;
        Long userId = getCurrentUserId();

        List<Object[]> results = repo.countPrescriptionsBetweenCustom(startDate, endDate, userId);

        Map<LocalDate, Long> counts = new LinkedHashMap<>();
        for (int i = 0; i < 10; i++) {
            counts.put(startDate.plusDays(i), 0L);
        }

        for (Object[] row : results) {
            LocalDate date = (LocalDate) row[0];
            Long count = (Long) row[1];
            counts.put(date, count);
        }

        return counts;
    }
}