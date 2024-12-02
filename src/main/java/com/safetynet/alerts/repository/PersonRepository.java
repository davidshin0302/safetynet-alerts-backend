package com.safetynet.alerts.repository;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.model.DataObject;
import com.safetynet.alerts.model.Person;
import jakarta.annotation.PostConstruct;
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

    public List<Person> findAll() {
        return personList;
    }

    @PostConstruct
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

    public Person findPerson(String firstName, String lastName) {
        return findByFirstAndLastName(firstName, lastName);
    }

    public boolean updateExistingPerson(Person person) {
        boolean updated = true;

        Person existingPerson = findByFirstAndLastName(person.getFirstName(), person.getLastName());

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

    public boolean delete(String firstName, String lastName) {
        boolean deleted = false;
        Person personToDelete = findByFirstAndLastName(firstName, lastName);

        if (personToDelete != null) {
            personList.remove(personToDelete);
            deleted = true;
        } else {
            log.error("Unable to find the person: {} {}", firstName, lastName);
        }
        return deleted;
    }

    public boolean save(Person person) {
        boolean result = false;

        if (!personList.contains(person)) {
            result = personList.add(person);
        } else {
            log.info("Person already exist in the data.");
        }

        return result;
    }

    private Person findByFirstAndLastName(String firstName, String lastName) {
        return personList.stream()
                .filter(existingPerson -> existingPerson.getFirstName().equalsIgnoreCase(firstName) && existingPerson.getLastName().equalsIgnoreCase(lastName))
                .findFirst()
                .orElse(null);
    }
}
