package com.andersenlab.patient.demo.service;

import com.andersenlab.patient.demo.exception.NameNotUniqueException;
import com.andersenlab.patient.demo.exception.NoActivePatientsException;
import com.andersenlab.patient.demo.exception.PatientNotFound;
import com.andersenlab.patient.demo.model.dto.PatientRequestDto;
import com.andersenlab.patient.demo.model.dto.PatientResponseDto;
import com.andersenlab.patient.demo.model.entity.Patient;
import com.andersenlab.patient.demo.repository.PatientRepository;
import com.andersenlab.patient.demo.util.PatientMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;

    @Override
    public PatientResponseDto createPatient(PatientRequestDto dto) {

        isNameUnique(dto.name());

        Patient patient = patientRepository.save(patientMapper.dtoToPatient(dto));

        return patientMapper.patientToDto(patient);
    }

    @Override
    public PatientResponseDto editPatient(long id, PatientRequestDto dto) {

        Patient patient = getPatientById(id);

        isNameUnique(id, dto.name());

        patient.setName(dto.name());
        patient.setBirthday(dto.birthday());
        patient.setTemperature(dto.temperature());
        patient.setPulse(dto.pulse());

        Patient newPatient = patientRepository.save(patient);

        return patientMapper.patientToDto(newPatient);
    }

    @Override
    public boolean markAsDeleted(long id) {
        Patient patient = getPatientById(id);

        patient.setDeleted(true);

        patientRepository.save(patient);

        return true;
    }

    @Override
    public List<PatientResponseDto> getAllActivePatients() {
        List<PatientResponseDto> list = new ArrayList<>();

        patientRepository.findAllByDeletedIsFalseOrderByNameAsc().
                forEach((patient) -> list.add(patientMapper.patientToDto(patient)));

        if (list.isEmpty()) {
            throw new NoActivePatientsException("There are no active patients in the DB");
        }

        return list;
    }

    private Patient getPatientById(long id) {
        return patientRepository.findByIdAndDeletedIsFalse(id)
                .orElseThrow(() -> new PatientNotFound("Patient with id '%d' was not found".formatted(id)));
    }

    private void isNameUnique(String name) {
        isNameUnique(-1, name);
    }

    private void isNameUnique(long id, String name) {
        Optional<Patient> patient = patientRepository.findByName(name);

        if (patient.isEmpty()) return;

        if (patient.get().getId() == id) return;

        throw new NameNotUniqueException("Patient with name '%s' already exist".formatted(name));
    }
}
