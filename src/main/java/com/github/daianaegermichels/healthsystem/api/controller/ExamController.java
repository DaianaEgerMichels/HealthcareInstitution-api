package com.github.daianaegermichels.healthsystem.api.controller;

import com.github.daianaegermichels.healthsystem.dto.ExamDTO;
import com.github.daianaegermichels.healthsystem.service.ExamService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@Api(value = "API REST Exams")
@RequestMapping("/api/exams")
@CrossOrigin(origins = "*")
public class ExamController {

    private ExamService examService;

    public ExamController(ExamService examService){
        this.examService = examService;
    }

    @PostMapping
    @ApiOperation(value = "Create a new exam")
    public ResponseEntity<ExamDTO> createExam(@Valid @RequestBody ExamDTO examDTO) {

        examService.save(examDTO);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(examDTO.getId()).toUri();

        return ResponseEntity.created(location).body(examDTO);
    }
}
