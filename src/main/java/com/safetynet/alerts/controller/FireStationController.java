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

    /**
     * Gets a list of all fire stations and returns them as a JSON response.
     *
     * @return A ResponseEntity containing a list of fire stations in JSON format
     *         on success, or a ResponseEntity with an internal server error status
     *         if an error occurs during serialization.
     * @throws IOException  If an error occurs during serialization of the fire station list.
     */
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

    /**
     * Adds a new fire station to the system.
     *
     * @param fireStation The fire station information to be added. This parameter must be a valid fire station object
     *                    annotated with @Valid and @RequestBody for validation.
     * @return A ResponseEntity containing the newly created fire station information in JSON format with a CREATED status
     *         on success, or a CONFLICT status if the fire station could not be saved.
     * @throws RuntimeException  If any unexpected error occurs during saving.
     */
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

    /**
     * Updates an existing fire station in the system.
     *
     * @param fireStation The updated fire station information. This parameter must be a valid fire station object
     *                    containing the address of the fire station to be updated and any other updated fields. The object
     *                    should be annotated with @RequestBody to indicate it's coming from the request body.
     * @return A ResponseEntity containing the updated fire station information in JSON format with a CREATED status
     *         on success, or a NOT_FOUND status if the fire station with the provided address could not be found.
     * @throws IOException  If an error occurs during retrieval of the updated fire station after the update.
     */
    @PutMapping
    public ResponseEntity<FireStation> updateExistingFireStation(@RequestBody FireStation fireStation) throws IOException {
        ResponseEntity<FireStation> responseEntity;

        log.info("...PUT request handling /firestation");

        if (fireStationRepository.updateExistingFireStationNumber(fireStation)) {
            responseEntity = ResponseEntity.status(HttpStatus.CREATED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(fireStation);

            log.info("processed PUT request /firestation request....");
        } else {
            log.warn("Unable to update the fire station: {}", fireStation);

            responseEntity = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return responseEntity;
    }

    /**
     * Deletes an existing fire station from the system based on its address.
     *
     * @param fireStation The address of the fire station to be deleted. This parameter is expected to be a String representing
     *                 the address in JSON format within the request body.
     * @return ResponseEntity with a NO_CONTENT status on successful deletion, or a NOT_FOUND status if
     *         the fire station with the provided address could not be found.
     * @throws IOException  If an error occurs during deserialization of the address from JSON format.
     */
    @DeleteMapping
    public ResponseEntity<HttpStatus> deleteExistingFireStation(@RequestBody FireStation fireStation) throws IOException {
        ResponseEntity<HttpStatus> responseEntity;

        log.info("...DELETE request handling /firestation");

        if (fireStationRepository.delete(fireStation.getAddress())) {
            responseEntity = ResponseEntity.noContent().build();

            log.info("Processed DELETE request /firestation");
        } else {
            log.warn("Unable delete fire station by provide address: {}", fireStation.getAddress());

            responseEntity = ResponseEntity.notFound().build();
        }

        return responseEntity;
    }
}
