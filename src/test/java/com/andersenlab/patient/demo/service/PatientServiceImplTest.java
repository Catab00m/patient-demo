package com.andersenlab.patient.demo.service;

import com.andersenlab.patient.demo.exception.NameNotUniqueException;
import com.andersenlab.patient.demo.exception.NoActivePatientsException;
import com.andersenlab.patient.demo.exception.PatientNotFound;
import com.andersenlab.patient.demo.model.dto.PatientRequestDto;
import com.andersenlab.patient.demo.model.dto.PatientResponseDto;
import com.andersenlab.patient.demo.model.entity.Patient;
import com.andersenlab.patient.demo.repository.PatientRepository;
import com.andersenlab.patient.demo.util.PatientMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PatientServiceImplTest {

    @InjectMocks
    PatientServiceImpl patientService;

    @Mock
    PatientMapper patientMapper;

    @Mock
    PatientRepository patientRepository;

    private static Patient patient;
    private static PatientRequestDto requestDto;
    private static PatientResponseDto responseDto;

    private static final long ID = 999;
    private static final long DIFFERENT_ID = 1000;
    private static final String NAME = "Test name";
    private static final LocalDate BIRTHDAY = LocalDate.parse("1998-06-01");
    private static final double TEMPERATURE = 36.6;
    private static final int PULSE = 60;

    @BeforeEach
    void setUp() {
        patient = Patient.builder()
                .id(ID)
                .name(NAME)
                .birthday(BIRTHDAY)
                .temperature(TEMPERATURE)
                .pulse(PULSE)
                .deleted(false)
                .build();

        requestDto = PatientRequestDto.builder()
                .name(NAME)
                .birthday(BIRTHDAY)
                .temperature(TEMPERATURE)
                .pulse(PULSE)
                .build();

        responseDto = PatientResponseDto.builder()
                .id(ID)
                .name(NAME)
                .birthday(BIRTHDAY)
                .temperature(TEMPERATURE)
                .pulse(PULSE)
                .build();
    }

    @Test
    void givenUniqueName_whenCreatingNewPatient_shouldReturnValidDto() {
        when(patientRepository.findByName(NAME)).thenReturn(Optional.empty());
        when(patientRepository.save(patient)).thenReturn(patient);
        when(patientMapper.dtoToPatient(requestDto)).thenReturn(patient);
        when(patientMapper.patientToDto(patient)).thenReturn(responseDto);

        PatientResponseDto patientResponseDto = patientService.createPatient(requestDto);

        assertEquals(responseDto, patientResponseDto);
        verify(patientMapper, times(1)).patientToDto(any());
        verify(patientMapper, times(1)).dtoToPatient(any());
        verify(patientRepository, times(1)).save(any());
        verify(patientRepository, times(1)).findByName(any());

    }

    @Test
    void givenNotUniqueName_whenCreatingNewPatient_shouldThrowException() {
        when(patientRepository.findByName(NAME)).thenReturn(Optional.of(patient));

        assertThrows(NameNotUniqueException.class, () -> patientService.createPatient(requestDto));

        verify(patientMapper, never()).patientToDto(any());
        verify(patientMapper, never()).dtoToPatient(any());
        verify(patientRepository, never()).save(any());
        verify(patientRepository, times(1)).findByName(any());
    }

    @Test
    void givenUniqueNameAndExistingId_whenEditingPatient_shouldReturnValidDto() {
        when(patientRepository.findByIdAndDeletedIsFalse(ID)).thenReturn(Optional.of(patient));
        when(patientRepository.findByName(NAME)).thenReturn(Optional.empty());
        when(patientRepository.save(patient)).thenReturn(patient);
        when(patientMapper.patientToDto(patient)).thenReturn(responseDto);

        PatientResponseDto patientResponseDto = patientService.editPatient(ID, requestDto);

        assertEquals(responseDto, patientResponseDto);
        verify(patientMapper, times(1)).patientToDto(any());
        verify(patientRepository, times(1)).save(any());
        verify(patientRepository, times(1)).findByName(any());
        verify(patientRepository, times(1)).findByIdAndDeletedIsFalse(anyLong());
    }

    @Test
    void givenNotUniqueNameAndSameId_whenEditingPatient_shouldReturnValidDto() {
        when(patientRepository.findByIdAndDeletedIsFalse(ID)).thenReturn(Optional.of(patient));
        when(patientRepository.findByName(NAME)).thenReturn(Optional.of(patient));
        when(patientRepository.save(patient)).thenReturn(patient);
        when(patientMapper.patientToDto(patient)).thenReturn(responseDto);

        PatientResponseDto patientResponseDto = patientService.editPatient(ID, requestDto);

        assertEquals(responseDto, patientResponseDto);
        verify(patientMapper, times(1)).patientToDto(any());
        verify(patientRepository, times(1)).save(any());
        verify(patientRepository, times(1)).findByName(any());
        verify(patientRepository, times(1)).findByIdAndDeletedIsFalse(anyLong());
    }

    @Test
    void givenNotUniqueNameAndDifferentId_whenEditingPatient_shouldThrowException() {
        when(patientRepository.findByIdAndDeletedIsFalse(DIFFERENT_ID)).thenReturn(Optional.of(patient));
        when(patientRepository.findByName(NAME)).thenReturn(Optional.of(patient));

        assertThrows(NameNotUniqueException.class, () -> patientService.editPatient(DIFFERENT_ID, requestDto));

        verify(patientMapper, never()).patientToDto(any());
        verify(patientRepository, never()).save(any());
        verify(patientRepository, times(1)).findByName(NAME);
        verify(patientRepository, times(1)).findByIdAndDeletedIsFalse(anyLong());
    }

    @Test
    void givenUniqueNameAndNonExistingId_whenEditingPatient_shouldThrowException() {
        when(patientRepository.findByIdAndDeletedIsFalse(ID)).thenReturn(Optional.empty());

        assertThrows(PatientNotFound.class, () -> patientService.editPatient(ID, requestDto));

        verify(patientMapper, never()).patientToDto(any());
        verify(patientRepository, never()).save(any());
        verify(patientRepository, never()).findByName(NAME);
        verify(patientRepository, times(1)).findByIdAndDeletedIsFalse(anyLong());
    }

    @Test
    void givenExistingId_whenDeletingPatient_shouldReturnTrue() {
        when(patientRepository.findByIdAndDeletedIsFalse(ID)).thenReturn(Optional.of(patient));
        when(patientRepository.save(patient)).thenReturn(patient);

        boolean result = patientService.markAsDeleted(ID);

        assertTrue(result);
        assertTrue(patient.isDeleted());
        verify(patientRepository, times(1)).findByIdAndDeletedIsFalse(anyLong());
        verify(patientRepository, times(1)).save(any());
    }

    @Test
    void givenNonExistingId_whenDeletingPatient_shouldThrowException() {
        when(patientRepository.findByIdAndDeletedIsFalse(DIFFERENT_ID)).thenReturn(Optional.empty());

        assertThrows(PatientNotFound.class, () -> patientService.markAsDeleted(DIFFERENT_ID));

        assertFalse(patient.isDeleted());
        verify(patientRepository, times(1)).findByIdAndDeletedIsFalse(anyLong());
        verify(patientRepository, never()).save(any());
    }

    @Test
    void givenPatientsListIsNotEmpty_whenGettingPatientsList_shouldReturnPatientsList() {
        when(patientRepository.findAllByDeletedIsFalseOrderByNameAsc()).thenReturn(List.of(patient, patient, patient));
        when(patientMapper.patientToDto(patient)).thenReturn(responseDto);

        List<PatientResponseDto> list = patientService.getAllActivePatients();

        assertEquals(3, list.size());
        verify(patientRepository, times(1)).findAllByDeletedIsFalseOrderByNameAsc();
        verify(patientMapper, times(3)).patientToDto(any());
    }

    @Test
    void givenPatientsListIsEmpty_whenGettingPatientsList_shouldThrowException() {
        when(patientRepository.findAllByDeletedIsFalseOrderByNameAsc()).thenReturn(List.of());

        assertThrows(NoActivePatientsException.class, () -> patientService.getAllActivePatients());

        verify(patientRepository, times(1)).findAllByDeletedIsFalseOrderByNameAsc();
        verify(patientMapper, never()).patientToDto(any());
    }
}