package com.github.daianaegermichels.healthsystem.repository;

import com.github.daianaegermichels.healthsystem.model.entity.Exam;
import com.github.daianaegermichels.healthsystem.model.entity.HealthcareInstitution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> {

    Optional<Exam> findByHealthcareInstitutionAndPatientNameAndProcedureNameAndPhysicianName(
            HealthcareInstitution healthcareInstitution,
            String patientName,
            String procedureName,
            String physicianName);

}
