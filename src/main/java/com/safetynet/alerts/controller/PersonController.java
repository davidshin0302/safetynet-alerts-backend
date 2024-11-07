package com.safetynet.alerts.controller;

import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.PersonRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("/person")
//TODO:: refactor!
public class PersonController {

    @Autowired
    PersonRepository personRepository;

    @GetMapping
    public ResponseEntity<List<Person>> getPeople() {
        return new ResponseEntity<>(personRepository.findAll(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<HttpStatus> addPerson(@Valid @RequestBody Person person) {
        return personRepository.save(person) ? new ResponseEntity<>(HttpStatus.ACCEPTED) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping
    public ResponseEntity<HttpStatus> updateExistingPerson(@RequestBody Person updatePerson) throws IOException {
        return personRepository.updateExistingPerson(updatePerson) ? new ResponseEntity<>(HttpStatus.ACCEPTED) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

//    @DeleteMapping
//    public ResponseEntity<HttpStatus> deleteExistingPerson(@RequestBody Person removePerson){
//        return personService.deleteExistingPerson(removePerson);
//    }
}
