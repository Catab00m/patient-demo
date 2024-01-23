package com.andersenlab.patient.demo.repository;

import com.andersenlab.patient.demo.model.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    Optional<Patient> findByIdAndDeletedIsFalse(long id);

    Optional<Patient> findByName(String name);

    List<Patient> findAllByDeletedIsFalseOrderByNameAsc();
}
