package com.safetynet.alerts.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.repository.MedicalRecordRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * Handle request for medical record.
 */
@RestController
@RequestMapping("/medicalRecord")
@Slf4j
public class MedicalRecordController {

    @Autowired
    private MedicalRecordRepository medicalRecordRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping
    public ResponseEntity<String> getAllMedicalRecords() {
        try {
            String medicalRecordList = objectMapper.writeValueAsString(medicalRecordRepository.findAll());
            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(medicalRecordList);
        } catch (IOException | RuntimeException ex) {
            log.error("Error at find all medical records: {}", ex.getMessage());
            return new ResponseEntity<>("[MedicalRecordController]: ", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Handle a new record to be add it.
     *
     * @param medicalRecord Medical Record to add.
     * @return Http status result
     */
    @PostMapping
    public ResponseEntity<HttpStatus> addMedicalRecords(@RequestBody MedicalRecord medicalRecord) {
        ResponseEntity<HttpStatus> response;

        if (medicalRecordRepository.save(medicalRecord)) {
            response = new ResponseEntity<>(HttpStatus.CREATED);
        } else {
            response = new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return response;
    }

}
