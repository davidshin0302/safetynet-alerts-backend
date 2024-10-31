package com.safetynet.alerts.service;

import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.PersonRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class PersonServiceImpl implements PersonService{
    @Autowired
    PersonRepository personRepository;

    @Override
    public List<Person> getAllPersonList(){
//        Person person1 = new Person("david", "shin", "1", "1", "1", "2","2");
//        Person person2 = new Person("minji", "kim", "2", "2", "2", "2","2");
//        personRepository.save(person1);
//        personRepository.save(person2);
        return  personRepository.findAll();
    }

    @Override
    public Person savePerson(Person person) {
        return personRepository.save(person);
    }
}
