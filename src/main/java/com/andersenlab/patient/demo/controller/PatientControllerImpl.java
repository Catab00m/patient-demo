package com.andersenlab.patient.demo.controller;

import com.andersenlab.patient.demo.model.dto.PatientRequestDto;
import com.andersenlab.patient.demo.model.dto.PatientResponseDto;
import com.andersenlab.patient.demo.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PatientControllerImpl implements PatientController {

    private final PatientService patientService;

    @Override
    public ResponseEntity<PatientResponseDto> createPatient(PatientRequestDto dto) {
        return new ResponseEntity<>(patientService.createPatient(dto), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<PatientResponseDto> editPatient(long id, PatientRequestDto dto) {
        return new ResponseEntity<>(patientService.editPatient(id, dto), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> markAsDeleted(long id) {
        patientService.markAsDeleted(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<PatientResponseDto>> getAllActivePatients() {
        return new ResponseEntity<>(patientService.getAllActivePatients(), HttpStatus.OK);
    }
}
