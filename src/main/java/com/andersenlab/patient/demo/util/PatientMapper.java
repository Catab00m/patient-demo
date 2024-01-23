package com.andersenlab.patient.demo.util;

import com.andersenlab.patient.demo.model.dto.PatientRequestDto;
import com.andersenlab.patient.demo.model.dto.PatientResponseDto;
import com.andersenlab.patient.demo.model.entity.Patient;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PatientMapper {
    Patient dtoToPatient(PatientRequestDto requestDto);

    PatientResponseDto patientToDto(Patient patient);
}
