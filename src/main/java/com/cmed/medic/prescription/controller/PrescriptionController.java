package com.cmed.medic.prescription.controller;

import jakarta.validation.Valid;
import com.cmed.medic.prescription.dto.*;
import com.cmed.medic.prescription.service.PrescriptionService;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/api/v1/prescription")
public class PrescriptionController {

    private final PrescriptionService service;

    public PrescriptionController(PrescriptionService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody PrescriptionRequest req) {
        return ResponseEntity.ok(service.create(req));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id,
            @RequestBody PrescriptionRequest req) {
        return ResponseEntity.ok(service.update(id, req));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOne(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping
    public ResponseEntity<?> getAll(@RequestParam(defaultValue = "0") int page) {
        return ResponseEntity.ok(service.getAll(page));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(Map.of("message", "Deleted successfully"));
    }

    @GetMapping("/last-10-days")
    public ResponseEntity<Map<LocalDate, Long>> getPrescriptionCounts(
            @RequestParam(value = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate referenceDate) {

        Map<LocalDate, Long> counts = service.getPrescriptionCounts(referenceDate);
        return ResponseEntity.ok(counts);
    }
}
