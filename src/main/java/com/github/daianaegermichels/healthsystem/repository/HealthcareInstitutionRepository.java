package com.github.daianaegermichels.healthsystem.repository;

import com.github.daianaegermichels.healthsystem.model.entity.HealthcareInstitution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HealthcareInstitutionRepository extends JpaRepository<HealthcareInstitution, Long> {
}
