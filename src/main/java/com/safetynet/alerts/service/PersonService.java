package com.safetynet.alerts.service;

import com.safetynet.alerts.model.Person;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;


public interface PersonService {
    ResponseEntity<List<Person>> getALlPersons();
    ResponseEntity<HttpStatus> createPerson();
    Person findPersonById(Long id);
}
