package com.github.daianaegermichels.healthsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExamDTO {

    private Long id;

    @NotNull(message = "Healthcare Institution Id is required")
    private Long institutionId;

    @NotBlank(message = "The patient's name is required")
    private String patientName;

    @NotNull(message = "The patient's age is required")
    private Integer patientAge;

    @NotBlank(message = "The patient's gender is required")
    private String patientGender;

    @NotBlank(message = "Physician name is required")
    private String physicianName;

    @NotBlank(message = "Physician's CRM number is required")
    private String physicianCRM;

    @NotBlank(message = "Procedure name is required")
    private String procedureName;
}
