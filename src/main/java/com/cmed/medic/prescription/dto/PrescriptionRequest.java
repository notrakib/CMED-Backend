package com.cmed.medic.prescription.dto;

public class PrescriptionRequest {

    private String patientName;
    private Integer patientAge;
    private String patientGender;
    private String diagnosis;
    private String medicines;
    private String nextVisitDate;

    public PrescriptionRequest() {
    }

    public PrescriptionRequest(String patientName, Integer patientAge, String patientGender,
                               String diagnosis, String medicines, String nextVisitDate) {
        this.patientName = patientName;
        this.patientAge = patientAge;
        this.patientGender = patientGender;
        this.diagnosis = diagnosis;
        this.medicines = medicines;
        this.nextVisitDate = nextVisitDate;
    }

    public String patientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public Integer patientAge() {
        return patientAge;
    }

    public void setPatientAge(Integer patientAge) {
        this.patientAge = patientAge;
    }

    public String patientGender() {
        return patientGender;
    }

    public void setPatientGender(String patientGender) {
        this.patientGender = patientGender;
    }

    public String diagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String medicines() {
        return medicines;
    }

    public void setMedicines(String medicines) {
        this.medicines = medicines;
    }

    public String nextVisitDate() {
        return nextVisitDate;
    }

    public void setNextVisitDate(String nextVisitDate) {
        this.nextVisitDate = nextVisitDate;
    }
}

