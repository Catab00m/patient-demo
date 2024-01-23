package com.andersenlab.patient.demo.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record PatientRequestDto(

        @Schema(name = "name", description = "Patient's name", example = "John Smith")
        @NotNull
        String name,

        @Schema(name = "birthday", description = "Patient's birthday", example = "1988-06-05")
        @NotNull
        LocalDate birthday,

        @Schema(name = "temperature", description = "Patient's temperature", example = "36.6")
        @NotNull
        @Min(0)
        double temperature,

        @Schema(name = "pulse", description = "Patient's pulse", example = "60")
        @NotNull
        @Min(0)
        int pulse
) {
}
