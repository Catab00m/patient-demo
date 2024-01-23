package com.andersenlab.patient.demo.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record PatientResponseDto (

        @Schema(name = "id", description = "Patient's id", example = "1")
        long id,

        @Schema(name = "name", description = "Patient's name", example = "John Smith")
        String name,

        @Schema(name = "birthday", description = "Patient's birthday", example = "1988-06-05")
        LocalDate birthday,

        @Schema(name = "temperature", description = "Patient's temperature", example = "36.6")
        double temperature,

        @Schema(name = "pulse", description = "Patient's pulse", example = "60")
        int pulse
) {
}
