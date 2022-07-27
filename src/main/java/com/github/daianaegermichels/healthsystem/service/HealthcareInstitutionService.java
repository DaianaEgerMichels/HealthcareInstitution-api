package com.github.daianaegermichels.healthsystem.service;

import com.github.daianaegermichels.healthsystem.dto.HealthcareInstitutionDTO;
import com.github.daianaegermichels.healthsystem.model.entity.HealthcareInstitution;
import com.github.daianaegermichels.healthsystem.repository.HealthcareInstitutionRepository;
import com.github.daianaegermichels.healthsystem.service.exception.EntityExistsException;
import com.github.daianaegermichels.healthsystem.service.exception.RequiredFieldMissingException;
import org.springframework.beans.BeanUtils;
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
    public void save(HealthcareInstitutionDTO healthcareInstitutionDTO){
        validateFields(healthcareInstitutionDTO);
        var healthcareInstitution = convertsToHealthcareInstitution(healthcareInstitutionDTO);
        isUniqueHealthcareInstitution(healthcareInstitution);
        healthcareInstitution.setPixeonCoins(20);
        healthcareInstitutionRepository.save(healthcareInstitution);
        healthcareInstitutionDTO.setId(healthcareInstitution.getId());
    }

    public Optional<HealthcareInstitution> getById(Long id){
        return healthcareInstitutionRepository.findById(id);
    }

    private void validateFields(HealthcareInstitutionDTO healthcareInstitutionDTO) {

        if(healthcareInstitutionDTO.getNameInstitution() == null || healthcareInstitutionDTO.getNameInstitution().trim().equals("")) {
            throw new RequiredFieldMissingException("Institution name is required!");
        }
        if(healthcareInstitutionDTO.getCnpj() == null || !(healthcareInstitutionDTO.getCnpj().length() == 14) || healthcareInstitutionDTO.getCnpj().isBlank()) {
            throw new RequiredFieldMissingException("CNPJ is required! The CNPJ entered is invalid!");
        }
    }

    private HealthcareInstitution convertsToHealthcareInstitution(HealthcareInstitutionDTO healthcareInstitutionDTO) {
        var healthcareInstitutionEntity = new HealthcareInstitution();
        BeanUtils.copyProperties(healthcareInstitutionDTO, healthcareInstitutionEntity);
        return healthcareInstitutionEntity;
    }

    private void isUniqueHealthcareInstitution(HealthcareInstitution healthcareInstitution) {
        var optionalHealthcareInstitution = healthcareInstitutionRepository
                .findByCnpj(healthcareInstitution.getCnpj());

        if(optionalHealthcareInstitution.isPresent()) {
            throw new EntityExistsException("There is already a Healthcare Institution registered with this CNPJ!");
        }
    }

    public HealthcareInstitution authenticateInstitution(HealthcareInstitutionDTO healthcareInstitutionDTO) {
        var healthcareInstitution = healthcareInstitutionRepository.findByCnpj(healthcareInstitutionDTO.getCnpj());
        if(!healthcareInstitution.get().getNameInstitution().equals(healthcareInstitutionDTO.getNameInstitution())){
            throw new EntityExistsException("Healthcare Institution not found for the name entered!");
        }

        if(!healthcareInstitution.get().getCnpj().equals(healthcareInstitutionDTO.getCnpj())){
            throw new EntityExistsException("Invalid CNPJ!");
        }

        return healthcareInstitution.get();

    }
}
