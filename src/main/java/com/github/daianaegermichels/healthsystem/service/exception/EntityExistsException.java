package com.github.daianaegermichels.healthsystem.service.exception;

public class ExamExistsException extends RuntimeException {
    public ExamExistsException(String message){
        super(message);
    }
}
