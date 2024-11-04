package com.safetynet.alerts.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.service.PersonService;
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
    public ResponseEntity<HttpStatus> createPerson(@RequestBody Person person){
        return personService.addNewPerson(person);
    }
}
