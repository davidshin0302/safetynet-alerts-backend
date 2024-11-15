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

    public List<Person> findAll() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String filePath = "src/main/resources/data.json";
            DataObject dataObject = objectMapper.readValue(new File(filePath), DataObject.class);
            return dataObject.getPersons();
        } catch (IOException | RuntimeException ex) {
            throw new RuntimeException(ex);
        }
    }

    public Person findByFirstAndLastName(Person person) {
        return findAll().stream()
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
            findAll().remove(person);
            deleted = true;
        } else {
            log.error("Unable to find the person");
        }
        return deleted;
    }

    public boolean save(Person person) {
        boolean result = false;

        if (findByFirstAndLastName(person) == null) {
            result = findAll().add(person);
        } else {
            log.error("Person already exist in the list");
        }
        return result;
    }
}
