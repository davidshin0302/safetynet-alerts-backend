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

    /**
     * Gets a list of all medical records and returns them as a JSON response.
     *
     * @return A ResponseEntity containing a list of medical records in JSON format
     *         on success, or a ResponseEntity with an internal server error status
     *         if an error occurs during serialization.
     * @throws IOException  If an error occurs during serialization of the medical record list.
     */
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
     * Adds a new medical record to the system.
     *
     * @param medicalRecord The medical record information to be added. This parameter must be a valid medical record object
     *                    annotated with @RequestBody for validation.
     * @return A ResponseEntity containing the newly created medical record information in JSON format with a CREATED status
     *         on success, or a CONFLICT status if the medical record could not be saved.
     * @throws RuntimeException  If any unexpected error occurs during saving.
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

    /**
     * Updates an existing medical record in the system.
     *
     * @param medicalRecord The updated medical record information. This parameter must be a valid medical record object
     *                    containing the address of the medical record to be updated and any other updated fields. The object
     *                    should be annotated with @RequestBody to indicate it's coming from the request body.
     * @return A ResponseEntity with a CREATED status on successful update, or a NOT_FOUND status if
     *         the medical record could not be found.
     */
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

    /**
     * Deletes an existing medical record from the system.
     *
     * @param medicalRecord The medical record information to be deleted. This parameter must be a valid medical record object
     *                   representing the record to be deleted. The object should be annotated with @RequestBody to indicate
     *                   it's coming from the request body.
     * @return A ResponseEntity with a NO_CONTENT status on successful deletion, or a NOT_FOUND status if
     *         the medical record could not be found.
     */
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
