package com.safetynet.alerts.repository;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.model.DataObject;
import com.safetynet.alerts.model.Person;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Repository
@Slf4j
public class PersonRepository {
    private final List<Person> personList;

    public PersonRepository() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String filePath = "src/main/resources/data.json";
            DataObject dataObject = objectMapper.readValue(new File(filePath), DataObject.class);
            personList = dataObject.getPersons();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Person> findAll() {
        return personList;
    }

    public Person findByFirstAndLastName(Person person) {
        return personList.stream()
                .filter(existingPerson -> existingPerson.equals(person))
                .findFirst()
                .orElse(null);
    }

    //TODO:: Should updateExistingPerson checks all the fields to be update? or not.
    public boolean updateExistingPerson(Person person) {
        boolean updated = true;

        Person existingPerson = findByFirstAndLastName(person);

        if (existingPerson != null) {
            existingPerson.setAddress(person.getAddress());
            existingPerson.setCity(person.getCity());
            existingPerson.setZip(person.getZip());
            existingPerson.setPhone(person.getPhone());
            existingPerson.setEmail(person.getEmail());

            save(existingPerson);
        } else {
            updated = false;
        }
        return updated;
    }

    public boolean delete(Person person) {
        boolean deleted = false;

        if (person != null) {
            personList.remove(person);
            deleted = true;
        } else {
            log.error("Unable to find the person");
        }
        return deleted;
    }

    public boolean save(Person person) {
        return personList.add(person);
    }
}
