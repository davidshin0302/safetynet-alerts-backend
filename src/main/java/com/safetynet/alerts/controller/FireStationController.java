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

/**
 * TODO:: Implement below request handler.
 * /firestation?stationNumber=<station_number> (GET): Retrieve a list of people by station.
 * /phoneAlert?firestation=<firestation_number> (GET): Get phone numbers by station.
 * /flood/stations?stations=<list_of_station_numbers> (GET): Get households by fire station(s).
 */
@RestController
@RequestMapping("/firestation")
@Slf4j
public class FireStationController {

    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private FireStationRepository fireStationRepository;

    @GetMapping
    public ResponseEntity<String> getFireStation() {

        try {
            String fireStationList = objectMapper.writeValueAsString(fireStationRepository.findAll());

            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(fireStationList);
        } catch (IOException | RuntimeException ex) {
            log.error("Error at serializing data: {}", ex.getMessage());
            return new ResponseEntity<>("[FireStationController]: ", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<FireStation> addFireStation(@Valid @RequestBody FireStation fireStation) {
        if (fireStationRepository.save(fireStation)) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(fireStation);
        } else {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @PutMapping
    public ResponseEntity<FireStation> updateExistingFireStation(@RequestBody FireStation fireStation) throws IOException {
        if (fireStationRepository.updateExistingFireStationNumber(fireStation)) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(fireStationRepository.findFireStation(fireStation.getAddress()));
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping
    public ResponseEntity<HttpStatus> deleteExistingFireStation(@RequestBody String address) throws IOException {
        FireStation fireStation = objectMapper.readValue(address, FireStation.class);
        if (fireStationRepository.delete(fireStation.getAddress())) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
