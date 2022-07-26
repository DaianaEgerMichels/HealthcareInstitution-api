package com.github.daianaegermichels.healthsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CNPJ;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HealthcareInstitutionDTO {

    private Long id;

    @NotBlank(message = "Institution name is required")
    private String nameInstitution;

    @Size(min = 14, max = 14)
    @NotBlank(message = "CNPJ is required")
    @CNPJ(message="The CNPJ entered is invalid")
    private String cnpj;

}
