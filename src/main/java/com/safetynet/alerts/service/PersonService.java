package com.safetynet.alerts.service;

import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.PersonRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class PersonService {

    @Autowired
    PersonRepository personRepository;

    public ResponseEntity<List<Person>> getPeople() {
        return new ResponseEntity<>(personRepository.findAll(), HttpStatus.OK);
    }

    public ResponseEntity<HttpStatus> addPerson(Person person) {
        personRepository.save(person);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    public ResponseEntity<HttpStatus> updateExistingPerson(Person updatePerson) {
        Person existingPerson = personRepository.findByFirstAndLastName(updatePerson);

        if (existingPerson != null) {
            existingPerson.setAddress(updatePerson.getAddress());
            existingPerson.setCity(updatePerson.getCity());
            existingPerson.setZip(updatePerson.getZip());
            existingPerson.setPhone(updatePerson.getPhone());
            existingPerson.setEmail(updatePerson.getEmail());

            personRepository.save(existingPerson);

            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<HttpStatus> deleteExistingPerson(Person removePerson) {
        personRepository.delete(removePerson);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
