package com.github.daianaegermichels.healthsystem.service.exception;

public class RequiredFieldMissingException extends RuntimeException{
    public RequiredFieldMissingException(String message){
        super(message);
    }
}
