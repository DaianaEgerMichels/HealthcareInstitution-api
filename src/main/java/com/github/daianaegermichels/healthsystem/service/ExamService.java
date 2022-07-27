package com.github.daianaegermichels.healthsystem.service;

import com.github.daianaegermichels.healthsystem.dto.ExamDTO;
import com.github.daianaegermichels.healthsystem.model.entity.Exam;
import com.github.daianaegermichels.healthsystem.model.entity.HealthcareInstitution;
import com.github.daianaegermichels.healthsystem.model.enums.PatientGender;
import com.github.daianaegermichels.healthsystem.repository.ExamRepository;
import com.github.daianaegermichels.healthsystem.repository.HealthcareInstitutionRepository;
import com.github.daianaegermichels.healthsystem.service.exception.*;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class ExamService {

    private final Integer COST_OF_OPERATION = 1;

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
        if(exam.getHealthcareInstitution().havePixeonCoins(COST_OF_OPERATION) == false) {
            throw new InsufficientPixeonCoinsException("Insufficient Pixeon Coins ($$) for this operation!");
        }
        var examSave = examRepository.save(exam);

        examDTO.setId(examSave.getId());

        examSave.getHealthcareInstitution().collectPixeonCoins(COST_OF_OPERATION);
        healthcareInstitutionRepository.save(examSave.getHealthcareInstitution());
    }

    public List<Exam> listAllExams(Long idHealthcareInstitution, Pageable pageable) {
        existsHealthcareInstitution(idHealthcareInstitution);
        return examRepository.findAllByHealthcareInstitution_Id(idHealthcareInstitution, pageable);
    }

    public void updateExam(ExamDTO examDTO, Long idExam) {
        examDTO.setId(idExam);
        var examExist = existsExamAndValidHealthcareInstitution(examDTO.getId(), examDTO.getInstitutionId());
        var examUpdate = examExist.get();
        examUpdate.setId(examDTO.getId());
        examUpdate.setHealthcareInstitution(healthcareInstitutionRepository.findById(examDTO.getInstitutionId()).get());
        examUpdate.setPatientName(examDTO.getPatientName());
        examUpdate.setPatientAge(examDTO.getPatientAge());
        examUpdate.setPatientGender(PatientGender.valueOf(examDTO.getPatientGender()));
        examUpdate.setPhysicianName(examDTO.getPhysicianName());
        examUpdate.setPhysicianCRM(examDTO.getPhysicianCRM());
        examUpdate.setProcedureName(examDTO.getProcedureName());
        var examValidate = convertExamToExamDto(examUpdate);
        validateExamDTO(examValidate);
        examRepository.save(examUpdate);
    }

    @Transactional
    public void deleteExam(Long idExam, Long idHealthcareInstitution){

        var examDelete = existsExamAndValidHealthcareInstitution(idExam, idHealthcareInstitution);
        examRepository.delete(examDelete.get());
    }

    public Optional<Exam> getById(Long idExam, Long idHealthcareInstitution) {
        var exam = existsExamAndValidHealthcareInstitution(idExam, idHealthcareInstitution);

        if(exam.get().isFirstRequest() == true && exam.get().getHealthcareInstitution().havePixeonCoins(COST_OF_OPERATION)==false) {
			throw new InsufficientPixeonCoinsException("Insufficient Pixeon Coins ($$) for this operation!");
		}

		if(exam.get().isFirstRequest() == true) {
			exam.get().getHealthcareInstitution().collectPixeonCoins(COST_OF_OPERATION);
			exam.get().setFirstRequest(false);
			examRepository.save(exam.get());
			healthcareInstitutionRepository.save(exam.get().getHealthcareInstitution());
		}

        return exam;
    }

    private void validateExamDTO(ExamDTO examDTO) {
        validateFields(examDTO);
        var healthcareInstitution = isHealthcareInstitutionValid(examDTO.getInstitutionId());
        isUniqueExam(examDTO, healthcareInstitution.get());
    }

    private void validateFields(ExamDTO examDTO) {
        if(examDTO.getInstitutionId() == null) {
            throw new RequiredFieldMissingException("Healthcare Institution Id is required!");
        }
        if(examDTO.getPatientName() == null || examDTO.getPatientName().trim().equals("")) {
            throw new RequiredFieldMissingException("The patient's name is required!");
        }
        if(examDTO.getPatientAge() == null || examDTO.getPatientAge() < 0) {
            throw new RequiredFieldMissingException("The patient's age is required. Enter a valid age!");
        }
        if(examDTO.getPatientGender() == null || examDTO.getPatientName().trim().equals("")) {
            throw new RequiredFieldMissingException("The patient's gender is required");
        }
        if(!examDTO.getPatientGender().equalsIgnoreCase("MALE") && !examDTO.getPatientGender().equalsIgnoreCase("FEMALE")) {
            throw new RequiredFieldMissingException("Enter a valid gender");
        }
        if(examDTO.getPhysicianName() == null || examDTO.getPhysicianName().trim().equals("")) {
            throw new RequiredFieldMissingException("Physician name is required!");
        }
        if(examDTO.getPhysicianCRM() == null || examDTO.getPhysicianCRM().trim().equals("")) {
            throw new RequiredFieldMissingException("Physician's CRM number is required!");
        }
        if(examDTO.getProcedureName() == null || examDTO.getProcedureName().trim().equals("")) {
            throw new RequiredFieldMissingException("Procedure name is required!");
        }
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
			throw new EntityExistsException("This exam already exist!");
		}
	}

    private Exam convertExamDtoToExam(ExamDTO examDTO) {
        var examEntity = new Exam();
        var healthcareInstitution = healthcareInstitutionRepository.findById(examDTO.getInstitutionId());
        examEntity.setHealthcareInstitution(healthcareInstitution.get());
        examEntity.setPatientName(examDTO.getPatientName());
        examEntity.setPatientAge(examDTO.getPatientAge());
        examEntity.setPatientGender(PatientGender.valueOf(examDTO.getPatientGender()));
        examEntity.setPhysicianName(examDTO.getPhysicianName());
        examEntity.setPhysicianCRM(examDTO.getPhysicianCRM());
        examEntity.setProcedureName(examDTO.getProcedureName());

        return examEntity;
    }

    private ExamDTO convertExamToExamDto(Exam exam) {
        var examDTO = new ExamDTO();
        Long healthcareInstitutionId = exam.getHealthcareInstitution().getId();
        examDTO.setInstitutionId(healthcareInstitutionId);
        examDTO.setPatientName(exam.getPatientName());
        examDTO.setPatientAge(exam.getPatientAge());
        if(exam.getPatientGender() != null) {
            examDTO.setPatientGender(exam.getPatientGender().toString());
        }
        examDTO.setPhysicianName(exam.getPhysicianName());
        examDTO.setPhysicianCRM(exam.getPhysicianCRM());
        examDTO.setProcedureName(exam.getProcedureName());

        return examDTO;
    }

    private void existsHealthcareInstitution(Long idHealthcareInstitution) {
        var healthcareInstitution = healthcareInstitutionRepository.findById(idHealthcareInstitution);

        if(!healthcareInstitution.isPresent()) {
            throw new EntityExistsException("Healthcare Institution does not exist!");
        }
    }

    private Optional<Exam> existsExamAndValidHealthcareInstitution(Long idExam , Long idHealthcareInstitution) {
        var examExist = examRepository.findById(idExam);

        if(!examExist.isPresent()) {
            throw new EntityNotFoundException("The exam does not exist.");
        }

        if(!examExist.get().getHealthcareInstitution().getId().equals(idHealthcareInstitution)) {
            throw new AccessDeniedException("You don't have permission to this operation!");
        }

        return examExist;
    }

}
