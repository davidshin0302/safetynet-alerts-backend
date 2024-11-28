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

@RestController
@RequestMapping("/medicalRecord")
@Slf4j
public class MedicalRecordController {

    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private MedicalRecordRepository medicalRecordRepository;

    @GetMapping
    public ResponseEntity<String> getAllMedicalRecords() {
        ResponseEntity<String> response;

        try {
            String medicalRecordList = objectMapper.writeValueAsString(medicalRecordRepository.findAll());
            response = ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(medicalRecordList);
        } catch (IOException | RuntimeException ex) {
            log.error("Error at find all medical records: {}", ex.getMessage());
            response = new ResponseEntity<>("[MedicalRecordController]: ", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    /**
     * Handle a new record to be add it.
     *
     * @param medicalRecord Medical Record to add.
     * @return Http status result
     */
    @PostMapping
    public ResponseEntity<HttpStatus> addMedicalRecord(@RequestBody MedicalRecord medicalRecord) {
        ResponseEntity<HttpStatus> response;

        if (medicalRecordRepository.save(medicalRecord)) {
            response = new ResponseEntity<>(HttpStatus.CREATED);
        } else {
            response = new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return response;
    }

    @PutMapping
    public ResponseEntity<HttpStatus> updateMedicalRecord(@RequestBody MedicalRecord medicalRecord) {
        ResponseEntity<HttpStatus> response;

        if (medicalRecordRepository.updateExistingMedicalRecord(medicalRecord)) {
            response = new ResponseEntity<>(HttpStatus.CREATED);
        } else {
            response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return response;
    }

    @DeleteMapping
    public ResponseEntity<HttpStatus> deleteMedicalRecord(@RequestBody MedicalRecord medicalRecord) {
        ResponseEntity<HttpStatus> response;

        if (medicalRecordRepository.delete(medicalRecord)) {
            response = new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return response;
    }
}
