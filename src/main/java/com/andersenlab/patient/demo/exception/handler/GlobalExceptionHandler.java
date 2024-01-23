package com.andersenlab.patient.demo.exception.handler;

import com.andersenlab.patient.demo.exception.NameNotUniqueException;
import com.andersenlab.patient.demo.exception.NoActivePatientsException;
import com.andersenlab.patient.demo.exception.PatientNotFound;
import com.andersenlab.patient.demo.model.dto.ExceptionResponseDto;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.OffsetDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ExceptionResponseDto handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {

        String errorMessage = e.getBindingResult()
                .getFieldErrors().stream()
                .map(fieldError -> fieldError.getField() + " " + fieldError.getDefaultMessage())
                .collect(Collectors.joining("; "));

        return ExceptionResponseDto.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message("Validation Exception")
                .exceptionMessage(errorMessage)
                .timestamp(OffsetDateTime.now())
                .build();
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ExceptionResponseDto handleConstraintViolationException(ConstraintViolationException e) {

        String errorMessage = e.getConstraintViolations()
                .stream()
                .map(constraintViolation ->
                        constraintViolation.getPropertyPath().toString()
                                .substring(constraintViolation.getPropertyPath().toString().lastIndexOf('.')+1) + " "
                                + constraintViolation.getMessage())
                .collect(Collectors.joining("; "));

        return ExceptionResponseDto.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message("Validation Exception")
                .exceptionMessage(errorMessage)
                .timestamp(OffsetDateTime.now())
                .build();
    }

    @ExceptionHandler(NameNotUniqueException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    ExceptionResponseDto handleNameNotUniqueException(NameNotUniqueException e) {
        return ExceptionResponseDto.builder()
                .statusCode(HttpStatus.CONFLICT.value())
                .message(HttpStatus.CONFLICT.getReasonPhrase())
                .exceptionMessage(e.getMessage())
                .timestamp(OffsetDateTime.now())
                .build();
    }

    @ExceptionHandler(NoActivePatientsException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    ExceptionResponseDto handleNoActivePatientsException(NoActivePatientsException e) {
        return ExceptionResponseDto.builder()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .message(HttpStatus.NOT_FOUND.getReasonPhrase())
                .exceptionMessage(e.getMessage())
                .timestamp(OffsetDateTime.now())
                .build();
    }

    @ExceptionHandler(PatientNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    ExceptionResponseDto handlePatientNotFound(PatientNotFound e) {
        return ExceptionResponseDto.builder()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .message(HttpStatus.NOT_FOUND.getReasonPhrase())
                .exceptionMessage(e.getMessage())
                .timestamp(OffsetDateTime.now())
                .build();
    }
}
