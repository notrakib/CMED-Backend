package com.cmed.medic.prescription.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageImpl;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.cmed.medic.prescription.dto.PrescriptionDTO;
import com.cmed.medic.prescription.dto.PrescriptionRequest;
import com.cmed.medic.utils.CustomExceptions.ResourceNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

@Repository
public class PrescriptionRepositoryImpl implements PrescriptionRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PrescriptionRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public PrescriptionDTO save(PrescriptionRequest dto, Long userId) {
        String sql = """
                    INSERT INTO prescription
                    (prescription_date, patient_name, patient_age, patient_gender, diagnosis, medicines, next_visit_date, user_id)
                    VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            var ps = connection.prepareStatement(sql, new String[] { "id" });
            ps.setObject(1, LocalDate.now());
            ps.setString(2, dto.patientName());
            ps.setInt(3, dto.patientAge());
            ps.setString(4, dto.patientGender());
            ps.setString(5, dto.diagnosis());
            ps.setString(6, dto.medicines());
            ps.setObject(7, dto.nextVisitDate());
            ps.setLong(8, userId);
            return ps;
        }, keyHolder);

        Long generatedId = keyHolder.getKey().longValue();

        return new PrescriptionDTO(
                generatedId,
                LocalDate.now(),
                dto.patientName(),
                dto.patientAge(),
                dto.patientGender(),
                dto.diagnosis(),
                dto.medicines(),
                dto.nextVisitDate() != null ? LocalDate.parse(dto.nextVisitDate()) : null);
    }

    @Override
    public PrescriptionDTO update(Long id, PrescriptionDTO dto, Long userId) {
        String sql = """
                    UPDATE prescription
                    SET prescription_date = ?,
                        patient_name = ?,
                        patient_age = ?,
                        patient_gender = ?,
                        diagnosis = ?,
                        medicines = ?,
                        next_visit_date = ?
                    WHERE id = ? AND user_id = ?
                """;

        int rows = jdbcTemplate.update(
                sql,
                dto.getPrescriptionDate(),
                dto.getPatientName(),
                dto.getPatientAge(),
                dto.getPatientGender(),
                dto.getDiagnosis(),
                dto.getMedicines(),
                dto.getNextVisitDate(),
                id,
                userId);

        if (rows == 0) {
            throw new ResourceNotFoundException("Prescription not found or not authorized to update");
        }

        return new PrescriptionDTO(
                id,
                LocalDate.now(),
                dto.getPatientName(),
                dto.getPatientAge(),
                dto.getPatientGender(),
                dto.getDiagnosis(),
                dto.getMedicines(),
                dto.getNextVisitDate());
    }

    @Override
    public Page<PrescriptionDTO> findAllByCreatedByIdCustom(Long userId, Pageable pageable) {

        String sql = """
                    SELECT id, prescription_date, patient_name, patient_age, patient_gender,
                           diagnosis, medicines, next_visit_date, user_id
                    FROM prescription
                    WHERE user_id = ?
                    LIMIT ? OFFSET ?
                """;

        List<PrescriptionDTO> rows = jdbcTemplate.query(
                sql,
                (rs, rowNum) -> new PrescriptionDTO(
                        rs.getLong("id"),
                        rs.getObject("prescription_date", LocalDate.class),
                        rs.getString("patient_name"),
                        rs.getInt("patient_age"),
                        rs.getString("patient_gender"),
                        rs.getString("diagnosis"),
                        rs.getString("medicines"),
                        rs.getObject("next_visit_date", LocalDate.class)),
                userId,
                pageable.getPageSize(),
                pageable.getOffset());

        Long total = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM prescription WHERE user_id = ?",
                Long.class,
                userId);

        return new PageImpl<>(rows, pageable, total);
    }

    @Override
    public Optional<PrescriptionDTO> findByIdAndCreatedByIdCustom(Long id, Long userId) {

        String sql = """
                    SELECT id, prescription_date, patient_name, patient_age, patient_gender,
                           diagnosis, medicines, next_visit_date, user_id
                    FROM prescription
                    WHERE id = ? AND user_id = ?
                """;

        List<PrescriptionDTO> rows = jdbcTemplate.query(
                sql,
                (rs, rowNum) -> new PrescriptionDTO(
                        rs.getLong("id"),
                        rs.getObject("prescription_date", LocalDate.class),
                        rs.getString("patient_name"),
                        rs.getInt("patient_age"),
                        rs.getString("patient_gender"),
                        rs.getString("diagnosis"),
                        rs.getString("medicines"),
                        rs.getObject("next_visit_date", LocalDate.class)),
                id,
                userId);

        return rows.isEmpty() ? Optional.empty() : Optional.of(rows.get(0));
    }

    @Override
    public List<Object[]> countPrescriptionsBetweenCustom(LocalDate startDate, LocalDate endDate, Long userId) {

        String sql = """
                    SELECT prescription_date, COUNT(*)
                    FROM prescription
                    WHERE prescription_date BETWEEN ? AND ?
                      AND user_id = ?
                    GROUP BY prescription_date
                    ORDER BY prescription_date ASC
                """;

        return jdbcTemplate.query(
                sql,
                (rs, rowNum) -> new Object[] {
                        rs.getObject("prescription_date", LocalDate.class),
                        rs.getLong(2)
                },
                startDate,
                endDate,
                userId);
    }

    @Override
    public int deleteByIdAndCreatedById(Long id) {
        String sql = """
                    DELETE FROM prescription
                    WHERE id = ?
                """;

        return jdbcTemplate.update(sql, id);
    }

}
