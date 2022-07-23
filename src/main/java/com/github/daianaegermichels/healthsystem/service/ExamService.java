package com.github.daianaegermichels.healthsystem.service;

import com.github.daianaegermichels.healthsystem.dto.ExamDTO;
import com.github.daianaegermichels.healthsystem.model.entity.Exam;
import com.github.daianaegermichels.healthsystem.model.entity.HealthcareInstitution;
import com.github.daianaegermichels.healthsystem.repository.ExamRepository;
import com.github.daianaegermichels.healthsystem.repository.HealthcareInstitutionRepository;
import com.github.daianaegermichels.healthsystem.service.exception.ExamExistsException;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ExamService {

    private ExamRepository examRepository;

    private HealthcareInstitutionRepository healthcareInstitutionRepository;

    public ExamService(ExamRepository examRepository, HealthcareInstitutionRepository healthcareInstitutionRepository){
        this.examRepository = examRepository;
        this.healthcareInstitutionRepository = healthcareInstitutionRepository;
    }

    public void save(ExamDTO examDTO){
        validateExamDTO(examDTO);
        var exam = convertExamDtoToExam(examDTO);
        examRepository.save(exam);
    }

    private void validateExamDTO(ExamDTO examDTO) {
        var healthcareInstitution = isHealthcareInstitutionValid(examDTO.getInstitutionId());
        isUniqueExam(examDTO, healthcareInstitution.get());
    }

    private Optional<HealthcareInstitution> isHealthcareInstitutionValid(Long institutionId) {
        var optionalHealthcareInstitution = healthcareInstitutionRepository.findById(institutionId);

        if(!optionalHealthcareInstitution.isPresent()) {
            throw new IllegalArgumentException("Healthcare Institution  Id is invalid!");
        }
        return optionalHealthcareInstitution;
    }


    private void isUniqueExam(ExamDTO examDTO, HealthcareInstitution healthcareInstitution) {
		var optionalExam = examRepository
                .findByHealthcareInstitutionAndPatientNameAndProcedureNameAndPhysicianName(
                healthcareInstitution,
                examDTO.getPatientName(),
                examDTO.getProcedureName(),
                examDTO.getPhysicianName());

		if(optionalExam.isPresent() && !optionalExam.get().getId().equals(examDTO.getId())) {
			throw new ExamExistsException("This exam already exist!");
		}
	}

    private Exam convertExamDtoToExam(ExamDTO examDTO) {
        var examEntity = new Exam();
        BeanUtils.copyProperties(examDTO, examEntity);
        return examEntity;
    }
}
