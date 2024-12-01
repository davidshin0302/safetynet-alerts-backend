package com.safetynet.alerts.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.PersonRepository;
import com.safetynet.alerts.service.PersonService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * TODO:: Implement below handler request.
 * /personInfo?firstName=<firstName>&lastName=<lastName> (GET): Retrieve detailed information about a person.
 * /communityEmail?city=<city> (GET): Get emails of people in a city.
 */
@RestController
@Slf4j
public class PersonController {

    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private PersonService personService;

    @GetMapping("/person")
    public ResponseEntity<String> getPeople() {
        try {
            String personList = objectMapper.writeValueAsString(personRepository.findAll());

            return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(personList);
        } catch (IOException | RuntimeException ex) {
            log.error("Error at serializing data: {}", ex.getMessage());
            return new ResponseEntity<>("[PersonController]: ", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/person")
    public ResponseEntity<Person> addPerson(@Valid @RequestBody Person person) {
        if (personRepository.save(person)) {
            return ResponseEntity.status(HttpStatus.CREATED).contentType(MediaType.APPLICATION_JSON).body(personRepository.findPerson(person.getFirstName(), person.getLastName(), person.getEmail()));
        } else {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @PutMapping("/person")
    public ResponseEntity<Person> updateExistingPerson(@RequestBody Person person) throws IOException {
        if (personRepository.updateExistingPerson(person)) {
            return ResponseEntity.status(HttpStatus.CREATED).contentType(MediaType.APPLICATION_JSON).body(personRepository.findPerson(person.getFirstName(), person.getLastName(), person.getEmail()));
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/person")
    public ResponseEntity<HttpStatus> deleteExistingPerson(@RequestBody Person person) {
        if (personRepository.delete(person.getFirstName(), person.getLastName(), person.getEmail())) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
