package com.andersenlab.patient.demo.exception;

public class NameNotUniqueException extends RuntimeException {

    public NameNotUniqueException() {
        super();
    }

    public NameNotUniqueException(String message) {
        super(message);
    }

    public NameNotUniqueException(String message, Throwable cause) {
        super(message, cause);
    }
}
