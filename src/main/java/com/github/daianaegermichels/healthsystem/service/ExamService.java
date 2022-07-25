package com.github.daianaegermichels.healthsystem.service;

import com.github.daianaegermichels.healthsystem.dto.ExamDTO;
import com.github.daianaegermichels.healthsystem.model.entity.Exam;
import com.github.daianaegermichels.healthsystem.model.entity.HealthcareInstitution;
import com.github.daianaegermichels.healthsystem.repository.ExamRepository;
import com.github.daianaegermichels.healthsystem.repository.HealthcareInstitutionRepository;
import com.github.daianaegermichels.healthsystem.service.exception.AccessDeniedException;
import com.github.daianaegermichels.healthsystem.service.exception.ExamExistsException;
import com.github.daianaegermichels.healthsystem.service.exception.ExamNotFoundException;
import com.github.daianaegermichels.healthsystem.service.exception.HealthcareInstitutionExistsException;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class ExamService {

    private ExamRepository examRepository;

    private HealthcareInstitutionRepository healthcareInstitutionRepository;

    public ExamService(ExamRepository examRepository, HealthcareInstitutionRepository healthcareInstitutionRepository){
        this.examRepository = examRepository;
        this.healthcareInstitutionRepository = healthcareInstitutionRepository;
    }

    @Transactional
    public void saveExam(ExamDTO examDTO){
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

    public List<Exam> listAllExams(Long idHealthcareInstitution, Pageable pageable) {
        existsHealthcareInstitution(idHealthcareInstitution);
        return examRepository.findAllByHealthcareInstitution_Id(idHealthcareInstitution, pageable);
    }

    private void existsHealthcareInstitution(Long idHealthcareInstitution) {
        var healthcareInstitution = healthcareInstitutionRepository.findById(idHealthcareInstitution);

        if(!healthcareInstitution.isPresent()) {
            throw new HealthcareInstitutionExistsException("Healthcare Institution does not exist!");
        }
    }

    public void updateExam(ExamDTO examDTO, Long idExam) {
        examDTO.setId(idExam);
        existsExamAndValidHealthcareInstitution(examDTO.getId(), examDTO.getInstitutionId());
        validateExamDTO(examDTO);
        var examUpdate = convertExamDtoToExam(examDTO);
        examRepository.save(examUpdate);
    }

    private void existsExamAndValidHealthcareInstitution(Long idExam , Long idHealthcareInstitution) {
        var examExist = examRepository.findById(idExam);

        if(!examExist.isPresent()) {
            throw new ExamNotFoundException("The exam does not exist.");
        }

        if(!examExist.get().getHealthcareInstitution().getId().equals(idHealthcareInstitution)) {
            throw new AccessDeniedException("You don't have permission to this operation!");
        }
    }

    @Transactional
    public void deleteExam(Long idExam, Long idHealthcareInstitution){

        existsExamAndValidHealthcareInstitution(idExam, idHealthcareInstitution);
        var examDelete = examRepository.findById(idExam);
        examRepository.delete(examDelete.get());
    }

    public Optional<Exam> getById(Long idExam) {
        return examRepository.findById(idExam);
    }
}
