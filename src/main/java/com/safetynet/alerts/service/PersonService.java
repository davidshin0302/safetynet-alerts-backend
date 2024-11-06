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

    public ResponseEntity<HttpStatus> addNewPerson(Person person) {
        personRepository.save(person);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    public ResponseEntity<HttpStatus> updateExistingPerson(Person updatePerson) {
        Long existingPersonId = findByFirstAndLastName(updatePerson);

        if (existingPersonId < 0) {
            log.info("Person doesn't not exit in DB");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        //findById return Optional<T> which is a container object that may or may not contain non-null value.
        personRepository.findById(existingPersonId).map(existingPerson -> {
            existingPerson.setAddress(updatePerson.getAddress());
            existingPerson.setCity(updatePerson.getCity());
            existingPerson.setZip(updatePerson.getZip());
            existingPerson.setPhone(updatePerson.getPhone());
            existingPerson.setEmail(updatePerson.getEmail());
            return personRepository.save(existingPerson);
        });

        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<HttpStatus> deleteExistingPerson(Person removePerson) {
        Long deletingPersonId = findByFirstAndLastName(removePerson);
        //TODO:: cod refactor might need as this logic repeats from the updateExistingPerson method.
        if (deletingPersonId < 0) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        personRepository.deleteById(deletingPersonId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public Long findByFirstAndLastName(Person searchPerson) {
        Long personId = -1L;

        for (Person person : personRepository.findAll()) {
            if ((searchPerson.getFirstName().equalsIgnoreCase(person.getFirstName())) && searchPerson.getLastName().equalsIgnoreCase(person.getLastName())) {
                personId = person.getId();
                break;
            }
        }
        return personId;
    }
}
