package com.github.daianaegermichels.healthsystem.service;

import com.github.daianaegermichels.healthsystem.model.entity.HealthcareInstitution;
import com.github.daianaegermichels.healthsystem.repository.HealthcareInstitutionRepository;
import com.github.daianaegermichels.healthsystem.service.exception.HealthcareInstitutionExistsException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class HealthcareInstitutionService {

    private HealthcareInstitutionRepository healthcareInstitutionRepository;

    public HealthcareInstitutionService(HealthcareInstitutionRepository healthcareInstitutionRepository){
        this.healthcareInstitutionRepository = healthcareInstitutionRepository;
    }

    @Transactional
    public void save(HealthcareInstitution healthcareInstitution){
        isUniqueHealthcareInstitution(healthcareInstitution);
        healthcareInstitutionRepository.save(healthcareInstitution);
    }

    public Optional<HealthcareInstitution> getById(Long id){
        return healthcareInstitutionRepository.findById(id);
    }

    private void isUniqueHealthcareInstitution(HealthcareInstitution healthcareInstitution) {
        var optionalHealthcareInstitution = healthcareInstitutionRepository
                .findByCnpj(healthcareInstitution.getCnpj());

        if(optionalHealthcareInstitution.isPresent()) {
            throw new HealthcareInstitutionExistsException("There is already a Healthcare Institution registered with this CNPJ!");
        }
    }
}
