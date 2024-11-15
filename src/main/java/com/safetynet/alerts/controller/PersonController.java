package com.safetynet.alerts.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.PersonRepository;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/person")
@Slf4j
public class PersonController {
    @Autowired
    private PersonRepository personRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping
    public ResponseEntity<String> getPeople() {

        try {
            String personList = objectMapper.writeValueAsString(personRepository.findAll());

            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(personList);
        } catch (IOException | RuntimeException ex) {
            log.error("Error at serializing data: {}", ex.getMessage());
            return new ResponseEntity<>("[PersonController]: ", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<Person> addPerson(@Valid @RequestBody Person person) {
        if (personRepository.save(person)) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(personRepository.findByFirstAndLastName(person));
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping
    public ResponseEntity<Person> updateExistingPerson(@RequestBody Person person) throws IOException {
        if (personRepository.updateExistingPerson(person)) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(personRepository.findByFirstAndLastName(person));
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping
    public ResponseEntity<HttpStatus> deleteExistingPerson(@RequestBody Person person) {
        if (personRepository.delete(person)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
