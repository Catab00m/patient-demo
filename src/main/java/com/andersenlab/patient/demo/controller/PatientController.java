package com.andersenlab.patient.demo.controller;

import com.andersenlab.patient.demo.model.dto.ExceptionResponseDto;
import com.andersenlab.patient.demo.model.dto.PatientRequestDto;
import com.andersenlab.patient.demo.model.dto.PatientResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping(path = "/api/v1")
@Validated
public interface PatientController {

    @Operation(
            summary = "Create new patient",
            description = "This endpoint is responsible for creating new patient",
            tags = { "Patient API" },
            responses = {
                    @ApiResponse(responseCode = "201", description = "New patient was created", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = PatientResponseDto.class))
                    }),
                    @ApiResponse(responseCode = "409", description = "Patient with this name already exist", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponseDto.class))
                    })
            }
    )
    @PostMapping(path = "/patient")
    ResponseEntity<PatientResponseDto> createPatient(@RequestBody @Valid PatientRequestDto dto);

    @Operation(
            summary = "Edit existing patient",
            description = "This endpoint is responsible for editing existing patient",
            tags = { "Patient API" },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Existing patient was edited", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = PatientResponseDto.class))
                    }),
                    @ApiResponse(responseCode = "404", description = "Patient with this id was not found", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponseDto.class))
                    })
            }
    )
    @PutMapping("/patient/{id}")
    ResponseEntity<PatientResponseDto> editPatient(@PathVariable @Min(1) long id, @RequestBody @Valid PatientRequestDto dto);

    @Operation(
            summary = "Delete existing patient",
            description = "This endpoint is responsible for deleting existing patient",
            tags = { "Patient API" },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Existing patient was deleted"),
                    @ApiResponse(responseCode = "404", description = "Patient with this id was not found", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponseDto.class))
                    })
            }
    )
    @DeleteMapping(path = "/patient/{id}")
    ResponseEntity<Void> markAsDeleted(@PathVariable @Min(1) long id);

    @Operation(
            summary = "Get all existing patients",
            description = "This endpoint is responsible for getting all existing patients",
            tags = { "Patient API" },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Existing patient was edited", content = {
                            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = PatientResponseDto.class)))
                    })
            }
    )
    @GetMapping("/patient")
    ResponseEntity<List<PatientResponseDto>> getAllActivePatients();

}
