package com.andersenlab.patient.demo.model.dto;

import lombok.Builder;

import java.time.OffsetDateTime;

@Builder
public record ExceptionResponseDto(

        int statusCode,

        String message,

        String exceptionMessage,

        OffsetDateTime timestamp
) {
}
