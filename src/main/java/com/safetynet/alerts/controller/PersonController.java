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


@RestController
@RequestMapping("/person")
@Slf4j
public class PersonController {

    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private PersonService personService;

    @GetMapping
    public ResponseEntity<String> getPeople() {
        ResponseEntity<String> responseEntity;

        try {
            String personList = objectMapper.writeValueAsString(personRepository.findAll());

            responseEntity = ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(personList);
        } catch (IOException | RuntimeException ex) {
            log.error("Error at serializing data: {}", ex.getMessage());
            responseEntity = new ResponseEntity<>("[PersonController]: ", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }


    @PostMapping
    public ResponseEntity<Person> addPerson(@Valid @RequestBody Person person) {
        ResponseEntity<Person> responseEntity;

        if (personRepository.save(person)) {
            responseEntity = ResponseEntity.status(HttpStatus.CREATED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(person);
        } else {
            responseEntity = new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return responseEntity;
    }

    @PutMapping
    public ResponseEntity<Person> updateExistingPerson(@RequestBody Person person) throws IOException {
        ResponseEntity<Person> responseEntity;

        if (personRepository.updateExistingPerson(person)) {
            responseEntity = ResponseEntity.status(HttpStatus.CREATED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(personRepository.findPerson(person.getFirstName(), person.getLastName()));
        } else {
            responseEntity = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return responseEntity;
    }

    @DeleteMapping
    public ResponseEntity<HttpStatus> deleteExistingPerson(@RequestBody Person person) {
        ResponseEntity<HttpStatus> responseEntity;

        if (personRepository.delete(person.getFirstName(), person.getLastName())) {
            responseEntity = ResponseEntity.noContent().build();
        } else {
            responseEntity = ResponseEntity.notFound().build();
        }
        return responseEntity;
    }
}
