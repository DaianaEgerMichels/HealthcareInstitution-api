package com.github.daianaegermichels.healthsystem.service.exception;

public class HealthcareInstitutionExistsException extends RuntimeException {
    public HealthcareInstitutionExistsException (String message){
        super(message);
    }
}
