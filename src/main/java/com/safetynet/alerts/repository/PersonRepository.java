package com.safetynet.alerts.repository;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.model.DataObject;
import com.safetynet.alerts.model.Person;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Repository
@Slf4j
public class PersonRepository {

    private final List<Person> personList = new ArrayList<>();

    public PersonRepository() {
        loadPersonData();
    }

    public List<Person> findAll() {
        return personList;
    }

    public Person findPerson(String firstName, String lastName, String email) {
        return findByFirstAndLastName(firstName, lastName, email);
    }

    public boolean updateExistingPerson(Person person) {
        boolean updated = true;

        for (Person existingPerson : personList) {
            if (existingPerson.equals(person)) {
                existingPerson.setAddress(person.getAddress());
                existingPerson.setCity(person.getCity());
                existingPerson.setZip(person.getZip());
                existingPerson.setPhone(person.getPhone());
                existingPerson.setEmail(person.getEmail());

                save(existingPerson);
            } else {
                updated = false;
            }
        }

        return updated;
    }

    public boolean delete(String firstName, String lastName, String email) {
        boolean deleted = false;
        Person person = findByFirstAndLastName(firstName, lastName, email);

        if (person != null) {
            deleted = personList.remove(person);
        } else {
            log.error("Unable to find the person: {} {}", firstName, lastName);
        }
        return deleted;
    }

    public boolean save(Person person) {
        boolean result = false;

        for (Person existingPerson : personList) {
            if (!existingPerson.equals(person)) {
                result = personList.add(person);
            } else {
                log.error("Person already exist in the list");
            }
        }
        return result;
    }

    private void loadPersonData() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String filePath = "src/main/resources/data.json";
            DataObject dataObject = objectMapper.readValue(new File(filePath), DataObject.class);
            personList.addAll(dataObject.getPersons());
        } catch (IOException | RuntimeException ex) {
            throw new RuntimeException(ex);
        }
    }

    private Person findByFirstAndLastName(String firstName, String lastName, String email) {
        return personList.stream()
                .filter(existingPerson -> existingPerson.getFirstName().equalsIgnoreCase(firstName) && existingPerson.getLastName().equalsIgnoreCase(lastName) && existingPerson.getEmail().equalsIgnoreCase(email))
                .findFirst()
                .orElse(null);
    }
}
