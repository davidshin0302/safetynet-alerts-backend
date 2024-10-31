package com.safetynet.alerts.service;

import com.safetynet.alerts.model.Person;

import java.util.List;


public interface PersonService {
    List<Person> getAllPersonList();
    Person savePerson(Person person);

}
