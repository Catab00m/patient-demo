package com.andersenlab.patient.demo.exception;

public class NoActivePatientsException extends RuntimeException {

    public NoActivePatientsException() {
        super();
    }

    public NoActivePatientsException(String message) {
        super(message);
    }

    public NoActivePatientsException(String message, Throwable cause) {
        super(message, cause);
    }
}
