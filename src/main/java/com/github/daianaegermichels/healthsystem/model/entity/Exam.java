package com.github.daianaegermichels.healthsystem.model.entity;

import com.github.daianaegermichels.healthsystem.model.enums.PatientGender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "exam")
public class Exam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="id_institution", referencedColumnName = "id")
    private HealthcareInstitution healthcareInstitution;

    @Column(name="patient_name")
    private String patientName;

    @Column(name="patient_age")
    private Integer patientAge;

    @Column(name="patient_gender")
    @Enumerated(value = EnumType.STRING)
    private PatientGender patientGender;

    @Column(name="physician_name")
    private String physicianName;

    @Column(name="physician_crm")
    private String physicianCRM;

    @Column(name="procedure_name")
    private String procedureName;

    @Column(name = "first_request")
    private boolean firstRequest = true;

    public boolean isFirstRequest() {
        return firstRequest;
    }

    public void setFirstRequest(boolean firstRequest) {
        this.firstRequest = firstRequest;
    }
}
