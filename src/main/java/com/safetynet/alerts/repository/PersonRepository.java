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

//TODO: Read and parse data json File into java instant class.
// Each class should have contructors, default etc...

@Repository
@Slf4j
public class PersonRepository {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String filePath = "src/main/resources/data.json";


    public List<Person> findAll() {
        try {
            return objectMapper.readValue(new File(filePath), DataObject.class).getPersons();
        } catch (IOException ex) {
            log.error(ex.getMessage());
        }

        return List.of();
    }

    public Person findById(Long id) {
        return findAll().stream().filter(person -> person.getId().equals(id)).findFirst().orElse(null);
    }

    public void deleteById(Long id) {
        List<Person> people = new ArrayList<Person>();

        findAll().forEach(person -> {
            if (!person.getId().equals(id)) {
                people.add(person);
            }
        });

        try {
            objectMapper.writeValue(new File(filePath), people);
        } catch (IOException ex) {
            log.error(ex.getMessage());
        }
    }

    public void save(Person person) {
        List<Person> people = findAll();

        try {
            people.add(person);
            objectMapper.writeValue(new File(filePath), people);
        } catch (IOException ex) {
            log.error(ex.getMessage());
        }
    }
}
