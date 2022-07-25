package com.github.daianaegermichels.healthsystem.service.exception;

public class EntityExistsException extends RuntimeException {
    public EntityExistsException(String message){
        super(message);
    }
}
