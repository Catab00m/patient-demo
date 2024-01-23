package com.andersenlab.patient.demo.exception;

public class PatientNotFound extends RuntimeException {

    public PatientNotFound() {
        super();
    }

    public PatientNotFound(String message) {
        super(message);
    }

    public PatientNotFound(String message, Throwable cause) {
        super(message, cause);
    }
}
