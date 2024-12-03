package com.safetynet.alerts.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.model.FireStation;
import com.safetynet.alerts.repository.FireStationRepository;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@RestController
@RequestMapping("/firestation")
@Slf4j
public class FireStationController {

    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private FireStationRepository fireStationRepository;

    @GetMapping
    public ResponseEntity<String> getFireStation() {
        ResponseEntity<String> responseEntity;

        log.info("...Get request handling /firestation");

        try {
            String fireStationList = objectMapper.writeValueAsString(fireStationRepository.findAll());

            responseEntity = ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(fireStationList);

            log.info("processed GET request /firestation request ...");
        } catch (IOException | RuntimeException ex) {
            log.error("Error at serializing data: {}", ex.getMessage());

            responseEntity = new ResponseEntity<>("[FireStationController]: ", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return responseEntity;
    }

    @PostMapping
    public ResponseEntity<FireStation> addFireStation(@Valid @RequestBody FireStation fireStation) {
        ResponseEntity<FireStation> responseEntity;

        log.info("...POST request handling /firestation");

        if (fireStationRepository.save(fireStation)) {
            responseEntity = ResponseEntity.status(HttpStatus.CREATED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(fireStation);

            log.info("processed POST request /firestation request....");
        } else {
            log.warn("Unable to save fire station:{}", fireStation);

            responseEntity = new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        return responseEntity;
    }

    @PutMapping
    public ResponseEntity<FireStation> updateExistingFireStation(@RequestBody FireStation fireStation) throws IOException {
        ResponseEntity<FireStation> responseEntity;

        log.info("...PUT request handling /firestation");

        if (fireStationRepository.updateExistingFireStationNumber(fireStation)) {
            responseEntity = ResponseEntity.status(HttpStatus.CREATED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(fireStationRepository.findFireStation(fireStation.getAddress()));

            log.info("processed PUT request /firestation request....");
        } else {
            log.warn("Unable to update the fire station: {}", fireStation);

            responseEntity = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return responseEntity;
    }

    @DeleteMapping
    public ResponseEntity<HttpStatus> deleteExistingFireStation(@RequestBody String address) throws IOException {
        ResponseEntity<HttpStatus> responseEntity;

        log.info("...DELETE request handling /firestation");

        FireStation fireStation = objectMapper.readValue(address, FireStation.class);
        if (fireStationRepository.delete(fireStation.getAddress())) {
            responseEntity = ResponseEntity.noContent().build();

            log.info("Processed DELETE request /firestation");
        } else {
            log.warn("Unable delete fire station: {}", fireStation);

            responseEntity = ResponseEntity.notFound().build();
        }

        return responseEntity;
    }
}
