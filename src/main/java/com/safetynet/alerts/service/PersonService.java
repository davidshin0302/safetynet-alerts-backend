package com.safetynet.alerts.service;

import com.safetynet.alerts.model.Person;

import java.util.List;


public interface PersonService {
    List<Person> getALlPersons();
    Person findPersonById(Long id);
    Person savePerson(Person person);
}
