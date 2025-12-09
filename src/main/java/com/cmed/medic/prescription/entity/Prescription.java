package com.cmed.medic.prescription.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

import com.cmed.medic.auth.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Prescription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate prescriptionDate;

    private String patientName;

    private Integer patientAge;

    private String patientGender;

    @Column(columnDefinition = "TEXT")
    private String diagnosis;

    @Column(columnDefinition = "TEXT")
    private String medicines;

    private LocalDate nextVisitDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User createdBy;

    public Prescription() {
    }

    public Prescription(LocalDate prescriptionDate, String patientName, Integer patientAge,
            String patientGender, String diagnosis, String medicines,
            LocalDate nextVisitDate) {
        this.prescriptionDate = prescriptionDate;
        this.patientName = patientName;
        this.patientAge = patientAge;
        this.patientGender = patientGender;
        this.diagnosis = diagnosis;
        this.medicines = medicines;
        this.nextVisitDate = nextVisitDate;
    }

    public Long getId() {
        return id;
    }

    public LocalDate getPrescriptionDate() {
        return prescriptionDate;
    }

    public String getPatientName() {
        return patientName;
    }

    public Integer getPatientAge() {
        return patientAge;
    }

    public String getPatientGender() {
        return patientGender;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public String getMedicines() {
        return medicines;
    }

    public LocalDate getNextVisitDate() {
        return nextVisitDate;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setPrescriptionDate(LocalDate prescriptionDate) {
        this.prescriptionDate = prescriptionDate;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public void setPatientAge(Integer patientAge) {
        this.patientAge = patientAge;
    }

    public void setPatientGender(String patientGender) {
        this.patientGender = patientGender;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public void setMedicines(String medicines) {
        this.medicines = medicines;
    }

    public void setNextVisitDate(LocalDate nextVisitDate) {
        this.nextVisitDate = nextVisitDate;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }
}