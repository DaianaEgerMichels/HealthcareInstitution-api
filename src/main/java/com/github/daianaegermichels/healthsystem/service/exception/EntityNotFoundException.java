package com.github.daianaegermichels.healthsystem.service.exception;

public class ExamNotFoundException extends RuntimeException{
    public ExamNotFoundException(String message){
        super(message);
    }
}
