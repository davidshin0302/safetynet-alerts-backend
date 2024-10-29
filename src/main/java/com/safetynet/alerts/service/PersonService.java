package com.safetynet.alerts.service;

import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.PersonRepository;


public interface PersonService {
    Person savePerson(Person person);

}
