package com.andersenlab.patient.demo.service;

import com.andersenlab.patient.demo.model.dto.PatientRequestDto;
import com.andersenlab.patient.demo.model.dto.PatientResponseDto;
import com.andersenlab.patient.demo.model.entity.Patient;

import java.util.List;

public interface PatientService {

    PatientResponseDto createPatient(PatientRequestDto dto);

    PatientResponseDto editPatient(long id, PatientRequestDto dto);

    boolean markAsDeleted(long id);

    List<PatientResponseDto> getAllActivePatients();

}
