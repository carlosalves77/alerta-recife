package com.carldev.alerta_recife.exception;

public class UserNotVerifiedException extends RuntimeException{
    public UserNotVerifiedException(String message) {
        super(message);
    }
}
