package com.safetynet.alerts.controller;

import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.service.PersonService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/person")
public class PersonController {

    @Autowired
    PersonService personService;

    @GetMapping
    public  ResponseEntity<List<Person>> getALlPersons() {
        return personService.getALlPersons();
    }

    @PostMapping
    public ResponseEntity<HttpStatus> addNewPerson(@Valid @RequestBody Person person){
        return personService.addNewPerson(person);
    }

    @PutMapping
    public ResponseEntity<HttpStatus> updateExistingPerson(@RequestBody Person updatePerson){
        return personService.updateExistingPerson(updatePerson);
    }

    @DeleteMapping
    public ResponseEntity<HttpStatus> deleteExistingPerson(@RequestBody Person removePerson){
        return personService.deleteExistingPerson(removePerson);
    }
}
