package com.safetynet.alerts.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.PersonRepository;
import com.safetynet.alerts.service.PersonInfoService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * This controller provides RESTful endpoints for managing person records.
 * It offers functionalities for retrieving a list of all person records,
 * adding a new person record, updating an existing one, and deleting a record.
 */
@RestController
@RequestMapping("/person")
@Slf4j
public class PersonController {

    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private PersonInfoService personInfoService;


    /**
     * Gets a list of all people and returns them as a JSON response.
     *
     * @return A ResponseEntity containing a list of people in JSON format on success,
     *         or a ResponseEntity with an internal server error status if an error occurs during serialization.
     * @throws IOException  If an error occurs during serialization of the people list.
     */
    @GetMapping
    public ResponseEntity<String> getPeople() {
        ResponseEntity<String> responseEntity;

        log.info("...Get request handling /person");

        try {
            String personList = objectMapper.writeValueAsString(personRepository.findAll());

            responseEntity = ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(personList);

            log.info("processed GET request /person request ...");
        } catch (IOException | RuntimeException ex) {
            log.error("Error at serializing data: {}", ex.getMessage());

            responseEntity = new ResponseEntity<>("[PersonController]: ", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }

    /**
     * Adds a new person to the system.
     *
     * @param person The person information to be added. This parameter must be a valid person object
     *                annotated with @Valid for validation.
     * @return A ResponseEntity containing the newly created person information in JSON format with a CREATED status
     *         on success, or a CONFLICT status if the person could not be saved.
     * @throws RuntimeException  If any unexpected error occurs during saving.
     */
    @PostMapping
    public ResponseEntity<Person> addPerson(@Valid @RequestBody Person person) {
        ResponseEntity<Person> responseEntity;

        log.info("...POST request handling /person");

        if (personRepository.save(person)) {
            responseEntity = ResponseEntity.status(HttpStatus.CREATED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(person);

            log.info("processed POST request /person request....");
        } else {
            log.warn("Unable to save person:{}", person);

            responseEntity = new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return responseEntity;
    }

    /**
     * Updates an existing person in the system.
     *
     * @param person The updated person information. This parameter must be a valid person object
     *                containing the information for the person to be updated. The object should be annotated with
     *                @RequestBody to indicate it's coming from the request body.
     * @return A ResponseEntity containing the updated person information in JSON format with a CREATED status
     *         on success, or a NOT_FOUND status if the person could not be found.
     * @throws IOException  If an error occurs during retrieval of the updated person after the update.
     */
    @PutMapping
    public ResponseEntity<Person> updateExistingPerson(@RequestBody Person person) throws IOException {
        ResponseEntity<Person> responseEntity;

        log.info("...PUT request handling /person");

        if (personRepository.updateExistingPerson(person)) {
            responseEntity = ResponseEntity.status(HttpStatus.CREATED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(personRepository.findPerson(person.getFirstName(), person.getLastName()));

            log.info("processed PUT request /person request....");
        } else {
            log.warn("Unable to update person:{}", person);

            responseEntity = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return responseEntity;
    }

    /**
     * Deletes an existing person from the system based on their first and last name.
     *
     * @param person A person object containing the first and last name of the person to be deleted.
     *                The object should be annotated with @RequestBody to indicate it's coming from the request body.
     * @return A ResponseEntity with a NO_CONTENT status on successful deletion, or a NOT_FOUND status if
     *         the person with the provided names could not be found.
     */
    @DeleteMapping
    public ResponseEntity<HttpStatus> deleteExistingPerson(@RequestBody Person person) {
        ResponseEntity<HttpStatus> responseEntity;

        log.info("...DELETE request handling /person");

        if (personRepository.delete(person.getFirstName(), person.getLastName())) {
            responseEntity = ResponseEntity.noContent().build();

            log.info("Processed DELETE request /person");
        } else {
            log.warn("Unable delete person: {}", person);

            responseEntity = ResponseEntity.notFound().build();
        }
        return responseEntity;
    }
}
