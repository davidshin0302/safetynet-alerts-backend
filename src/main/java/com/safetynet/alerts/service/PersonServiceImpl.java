package com.safetynet.alerts.service;

import com.safetynet.alerts.Exception.ResourceNotFoundException;
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
public class PersonServiceImpl implements PersonService{

    @Autowired
    PersonRepository personRepository;

    @Override
    public ResponseEntity<List<Person>> getALlPersons(){
        Person person1 = new Person(1L,"david", "shin", "1", "1", "1", "2","2");
        Person person2 = new Person(2L, "minji", "kim", "2", "2", "2", "2","2");
        personRepository.save(person1);
        personRepository.save(person2);
        return  new ResponseEntity<List<Person>>(personRepository.findAll(), HttpStatus.FOUND);
    }

    @Override
    public ResponseEntity<HttpStatus> createPerson() {
        //TODO:: Need implmentation how to create person from interactino.
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Override
    public Person findPersonById(Long id){
        System.out.println(personRepository.findById(1L));
       return personRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Person is not found " + id));
    }
}
