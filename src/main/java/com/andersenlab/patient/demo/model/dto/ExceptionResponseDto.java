package com.andersenlab.patient.demo.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.OffsetDateTime;

@Builder
public record ExceptionResponseDto(

        @Schema(name = "statusCode", description = "Http Status Code", example = "404")
        int statusCode,

        @Schema(name = "message", description = "Exception message", example = "Not Found")
        String message,

        @Schema(name = "exceptionMessage", description = "Detailed exception message", example = "Patient with id '1' not found")
        String exceptionMessage,

        @Schema(name = "timestamp", description = "Exception timestamp", example = "2024-01-01T00:00:00.123Z")
        OffsetDateTime timestamp
) {
}
