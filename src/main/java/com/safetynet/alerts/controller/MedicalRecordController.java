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
        ResponseEntity<String> responseEntity;

        log.info("...Get request handling /medicalRecord");

        try {
            String medicalRecordList = objectMapper.writeValueAsString(medicalRecordRepository.findAll());
            responseEntity = ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(medicalRecordList);

            log.info("processed GET request /medicalRecord request ...");
        } catch (IOException | RuntimeException ex) {
            log.error("Error at find all medical records: {}", ex.getMessage());
            responseEntity = new ResponseEntity<>("[MedicalRecordController]: ", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }

    /**
     * Handle a new record to be added it.
     *
     * @param medicalRecord Medical Record to add.
     * @return Http status result
     */
    @PostMapping
    public ResponseEntity<MedicalRecord> addMedicalRecord(@RequestBody MedicalRecord medicalRecord) {
        ResponseEntity<MedicalRecord> responseEntity;

        log.info("...POST request handling /medicalRecord");

        if (medicalRecordRepository.save(medicalRecord)) {
            responseEntity = ResponseEntity.status(HttpStatus.CREATED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(medicalRecord);

            log.info("processed POST request /medicalRecord request....");
        } else {
            log.warn("Unable to save medical record:{}", medicalRecord);
            responseEntity = new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return responseEntity;
    }

    @PutMapping
    public ResponseEntity<HttpStatus> updateMedicalRecord(@RequestBody MedicalRecord medicalRecord) {
        ResponseEntity<HttpStatus> responseEntity;

        log.info("...PUT request handling /medicalRecord");

        if (medicalRecordRepository.updateExistingMedicalRecord(medicalRecord)) {
            responseEntity = new ResponseEntity<>(HttpStatus.CREATED);

            log.info("processed PUT request /medicalRecord request....");
        } else {
            log.warn("Unable to update the medical record: {}", medicalRecord);

            responseEntity = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return responseEntity;
    }

    @DeleteMapping
    public ResponseEntity<HttpStatus> deleteMedicalRecord(@RequestBody MedicalRecord medicalRecord) {
        ResponseEntity<HttpStatus> responseEntity;

        log.info("...DELETE request handling /medicalRecord");

        if (medicalRecordRepository.delete(medicalRecord)) {
            responseEntity = new ResponseEntity<>(HttpStatus.NO_CONTENT);

            log.info("Processed DELETE request /medicalRecord");
        } else {
            log.warn("Unable delete medical record: {}", medicalRecord);

            responseEntity = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return responseEntity;
    }
}
