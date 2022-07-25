package com.github.daianaegermichels.healthsystem.api.controller;

import com.github.daianaegermichels.healthsystem.dto.ExamDTO;
import com.github.daianaegermichels.healthsystem.model.entity.Exam;
import com.github.daianaegermichels.healthsystem.service.ExamService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.List;

@RestController
@Api(value = "API REST Exams")
@RequestMapping("/api/exams")
@CrossOrigin(origins = "*")
public class ExamController {

    private ExamService examService;

    public ExamController(ExamService examService) {
        this.examService = examService;
    }

    @PostMapping
    @ApiOperation(value = "Create a new exam")
    public ResponseEntity<ExamDTO> createExam(@Valid @RequestBody ExamDTO examDTO) {

        examService.saveExam(examDTO);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(examDTO.getId()).toUri();

        return ResponseEntity.created(location).body(examDTO);
    }

    @GetMapping("/{id_institution}")
    @ApiOperation(value = "List exams by Healthcare Institution")
    public ResponseEntity<List<Exam>> getAllExams(@PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
                                                  @NotNull @PathVariable(name = "id_institution") Long idHealthcareInstitution) {

        var examsList = examService.listAllExams(idHealthcareInstitution, pageable);
        if (examsList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(examsList);
    }

    @PutMapping("/{id_exam}")
    @ApiOperation(value = "Update an exam")
    public ResponseEntity<ExamDTO> updateExam(@NotNull @PathVariable(name = "id_exam") Long idExam,
                                              @Valid @RequestBody ExamDTO examDTO) {

        examService.updateExam(examDTO, idExam);
        return ResponseEntity.ok(examDTO);
    }

    @DeleteMapping("/{id_exam}")
    @ApiOperation(value = "Delete an exam by Id")
    public ResponseEntity deleteExam(@NotNull @PathVariable(name = "id_exam") Long idExam,
                                     @NotNull @RequestParam("id_institution")Long idHealthcareInstitution) {

        examService.deleteExam(idExam, idHealthcareInstitution);
        return ResponseEntity.ok("Exam deleted successfully!");

    }

    @GetMapping("/{id_exam}")
    @ApiOperation(value = "Get an Exam by Id")
    public ResponseEntity<Exam> getExam(@PathVariable(name= "id_exam") Long idExam){
        var exam = examService.getById(idExam);
        if(!exam.isPresent()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(exam.get());
    }
}
