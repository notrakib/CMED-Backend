package com.cmed.medic.prescription.validation;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import com.cmed.medic.prescription.dto.PrescriptionRequest;
import com.cmed.medic.utils.ValidationResult;

public class PrescriptionValidation {

    public static ValidationResult validatePatientName(String name) {
        if (name == null || name.isBlank()) {
            return new ValidationResult(false, "Patient name is mandatory");
        }
        for (char c : name.toCharArray()) {
            if (Character.isDigit(c)) {
                return new ValidationResult(false, "Patient name cannot contain numbers");
            }
        }
        return new ValidationResult(true, "Valid patient name");
    }

    public static ValidationResult validatePatientAge(Integer age) {
        if (age == null) {
            return new ValidationResult(false, "Patient age is mandatory");
        }
        if (age < 1 || age >= 121) {
            return new ValidationResult(false, "Patient age must be between 1 and 120");
        }
        return new ValidationResult(true, "Valid patient age");
    }

    public static ValidationResult validatePatientGender(String gender) {
        if (gender == null || gender.isBlank()) {
            return new ValidationResult(false, "Patient gender is mandatory");
        }
        String g = gender.trim().toLowerCase();
        if (!g.equals("male") && !g.equals("female")) {
            return new ValidationResult(false, "Patient gender must be 'male' or 'female'");
        }
        return new ValidationResult(true, "Valid patient gender");
    }

    public static ValidationResult validateDiagnosis(String diagnosis) {
        if (diagnosis == null) {
            return new ValidationResult(false, "Diagnosis field cannot be null");
        }
        return new ValidationResult(true, "Valid diagnosis");
    }

    public static ValidationResult validateMedicines(String medicines) {
        if (medicines == null) {
            return new ValidationResult(false, "Medicines field cannot be null");
        }
        return new ValidationResult(true, "Valid medicines");
    }

    public static ValidationResult validateNextVisitDate(String nextVisitDate) {
        try {
            LocalDate date = LocalDate.parse(nextVisitDate);

            if (date != null && date.isBefore(LocalDate.now())) {
                return new ValidationResult(false, "Next visit date cannot be in the past");
            }

            return new ValidationResult(true, "Valid next visit date");
        } catch (DateTimeParseException ex) {
            return new ValidationResult(false, "Invalid date format. Use yyyy-MM-dd");
        }
    }

    public static ValidationResult validatePrescriptionRequest(PrescriptionRequest req) {
        ValidationResult result;

        result = validatePatientName(req.patientName());
        if (!result.isValid())
            return result;

        result = validatePatientAge(req.patientAge());
        if (!result.isValid())
            return result;

        result = validatePatientGender(req.patientGender());
        if (!result.isValid())
            return result;

        result = validateDiagnosis(req.diagnosis());
        if (!result.isValid())
            return result;

        result = validateMedicines(req.medicines());
        if (!result.isValid())
            return result;

        result = validateNextVisitDate(req.nextVisitDate());
        if (!result.isValid())
            return result;

        return new ValidationResult(true, "Prescription request is valid");
    }

    public static ValidationResult validatePrescriptionUpdateRequest(PrescriptionRequest req) {

        if (req.patientName() != null) {
            ValidationResult r = validatePatientName(req.patientName());
            if (!r.isValid())
                return r;
        }

        if (req.patientAge() != null) {
            ValidationResult r = validatePatientAge(req.patientAge());
            if (!r.isValid())
                return r;
        }

        if (req.patientGender() != null) {
            ValidationResult r = validatePatientGender(req.patientGender());
            if (!r.isValid())
                return r;
        }

        if (req.diagnosis() != null) {
            ValidationResult r = validateDiagnosis(req.diagnosis());
            if (!r.isValid())
                return r;
        }

        if (req.medicines() != null) {
            ValidationResult r = validateMedicines(req.medicines());
            if (!r.isValid())
                return r;
        }

        if (req.nextVisitDate() != null) {
            ValidationResult r = validateNextVisitDate(req.nextVisitDate());
            if (!r.isValid())
                return r;
        }

        return new ValidationResult(true, "PATCH update request is valid");
    }
}
