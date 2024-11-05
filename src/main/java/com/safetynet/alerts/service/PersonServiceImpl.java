package com.safetynet.alerts.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.Exception.GlobalExceptionHandler;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.PersonRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

@Slf4j
@Service
public class PersonServiceImpl implements PersonService {

    @Autowired
    PersonRepository personRepository;

    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public ResponseEntity<List<Person>> getALlPersons() {
        Person person1 = new Person(1L, "david", "shin", "1", "1", "1", "2", "2");
        Person person2 = new Person(2L, "minji", "kim", "2", "2", "2", "2", "2");
        personRepository.save(person1);
        personRepository.save(person2);
        return new ResponseEntity<List<Person>>(personRepository.findAll(), HttpStatus.FOUND);
    }

    @Override
    public ResponseEntity<HttpStatus> addNewPerson(Person person) {
        personRepository.save(person);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<HttpStatus> updateExistingPerson(Person updatePerson) {
        Long personId = findByFirstAndLastName(updatePerson);

        if (personId < 0) {
            log.info("Person doesn't not exit in DB");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        //findById return Optional<T> which is a container object that may or may not contain non-null value.
        personRepository.findById(personId).map(existingPerson ->{
            existingPerson.setAddress(updatePerson.getAddress());
            existingPerson.setCity(updatePerson.getCity());
            existingPerson.setZip(updatePerson.getZip());
            existingPerson.setPhone(updatePerson.getPhone());
            existingPerson.setEmail(updatePerson.getEmail());
            return personRepository.save(existingPerson);
        });

        return new ResponseEntity<>(HttpStatus.OK);
    }

    public Long findByFirstAndLastName(Person updatePerson) {
        Long personId = -1L;
        for (Person person : personRepository.findAll()) {
            if ((updatePerson.getFirstName().equalsIgnoreCase(person.getFirstName())) && (person.getLastName().equalsIgnoreCase(person.getLastName()))) {
                personId = person.getId();
                break;
            }
        }
        return personId;
    }
}
