package com.andersenlab.patient.demo.controller;

import com.andersenlab.patient.demo.model.dto.PatientRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PatientControllerTest {

    private static ObjectMapper mapper;

    @Autowired
    private MockMvc mockMvc;

    private static final String NAME = "Test name";
    public static final String DIFFERENT_NAME = "Test-2 name";
    private static final LocalDate BIRTHDAY = LocalDate.parse("1998-06-01");
    private static final LocalDate DIFFERENT_BIRTHDAY = LocalDate.parse("2000-02-18");
    private static final double TEMPERATURE = 36.6;
    private static final int PULSE = 60;
    private static final int INVALID_PULSE = -1;

    @Container
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest");

    @BeforeAll
    static void init() {
        postgres.start();

        mapper = new ObjectMapper().findAndRegisterModules();
    }

    @AfterAll
    static void tearDown() {
        postgres.stop();
    }

    @BeforeEach
    void setUp() {

    }

    @Test
    @Order(1)
    void givenCorrectInput_whenCreatingPatient_shouldReturnStatusCreated() throws Exception {
        PatientRequestDto requestDto = PatientRequestDto.builder()
                        .name(NAME)
                        .birthday(BIRTHDAY)
                        .temperature(TEMPERATURE)
                        .pulse(PULSE)
                        .build();

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/patient")
                        .content(mapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated());
    }

    @Test
    @Order(2)
    void givenDuplicateName_whenCreatingPatient_shouldReturnStatusConflict() throws Exception {
        PatientRequestDto requestDto = PatientRequestDto.builder()
                .name(NAME)
                .birthday(BIRTHDAY)
                .temperature(TEMPERATURE)
                .pulse(PULSE)
                .build();

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/patient")
                        .content(mapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
    }

    @Test
    @Order(3)
    void givenInvalidInput_whenCreatingPatient_shouldReturnStatusBadRequest() throws Exception {
        PatientRequestDto invalidRequestDto = PatientRequestDto.builder()
                .name(NAME)
                .birthday(BIRTHDAY)
                .temperature(TEMPERATURE)
                .pulse(INVALID_PULSE)
                .build();

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/patient")
                        .content(mapper.writeValueAsString(invalidRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(4)
    void givenCorrectInput_whenEditingPatient_shouldReturnStatusOk() throws Exception {
        PatientRequestDto requestDto = PatientRequestDto.builder()
                .name(NAME)
                .birthday(DIFFERENT_BIRTHDAY)
                .temperature(TEMPERATURE)
                .pulse(PULSE)
                .build();

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/v1/patient/1")
                        .content(mapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(5)
    void givenDuplicateName_whenEditingPatient_shouldReturnStatusConflict() throws Exception {
        PatientRequestDto createRequestDto = PatientRequestDto.builder()
                .name(DIFFERENT_NAME)
                .birthday(BIRTHDAY)
                .temperature(TEMPERATURE)
                .pulse(PULSE)
                .build();

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/patient")
                        .content(mapper.writeValueAsString(createRequestDto))
                        .contentType(MediaType.APPLICATION_JSON));


        PatientRequestDto editRequestDto = PatientRequestDto.builder()
                .name(NAME)
                .birthday(DIFFERENT_BIRTHDAY)
                .temperature(TEMPERATURE)
                .pulse(PULSE)
                .build();

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/v1/patient/2")
                        .content(mapper.writeValueAsString(editRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
    }

    @Test
    @Order(6)
    void givenCorrectId_whenDeletingPatient_shouldReturnStatusOk() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/v1/patient/2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(7)
    void givenCorrectId_whenDeletingPatient_shouldReturnStatusNotFound() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/v1/patient/2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(8)
    void givenNotEmptyList_whenGettingPatientsList_shouldReturnStatusOk() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/patient")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(9)
    void givenEmptyList_whenGettingPatientsList_shouldReturnStatusNotFound() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/v1/patient/1")
                        .contentType(MediaType.APPLICATION_JSON));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/patient")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}