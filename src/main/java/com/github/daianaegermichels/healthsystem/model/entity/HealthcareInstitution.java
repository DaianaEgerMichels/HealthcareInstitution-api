package com.github.daianaegermichels.healthsystem.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CNPJ;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "healthcare_institution")
public class HealthcareInstitution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name_institution")
    @NotBlank(message = "Institution name is required")
    private String nameInstitution;

    @Size(min = 14, max = 14)
    @NotBlank(message = "CNPJ is required")
    @CNPJ(message="The CNPJ entered is invalid")
    @Column(name="cnpj", unique = true, length = 14)
    private String cnpj;

    @NotNull
    @Column(name = "pixeon_coins")
    private Integer pixeonCoins = 20;

    public void collectPixeonCoins(Integer pixeonCoins) {
        this.pixeonCoins = this.pixeonCoins - pixeonCoins;
    }

    public boolean havePixeonCoins(Integer pixeonCoins) {
        return (this.pixeonCoins - pixeonCoins) >= 0;
    }
}
