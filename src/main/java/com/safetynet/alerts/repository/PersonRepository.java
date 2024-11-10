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
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String filePath = "src/main/resources/data.json";
    private List<Person> personList;

    public PersonRepository() {
        try {
            DataObject dataObject = objectMapper.readValue(new File(filePath), DataObject.class);
            personList =  dataObject.getPersons();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Person> findAll() {
        return personList;
    }

    public Person findByFirstAndLastName(Person findPerson) {
        return personList.stream()
                .filter(existingPerson -> existingPerson.equals(findPerson))
                .findFirst()
                .orElse(null);
    }
    //TODO:: Should updateExistingPerson checks all the fields to be update? or not.
    public boolean updateExistingPerson(Person updatePerson) throws IOException {
        Person existingPerson = findByFirstAndLastName(updatePerson);
        boolean updated = true;

        if (existingPerson != null) {
            existingPerson.setAddress(updatePerson.getAddress());
            existingPerson.setCity(updatePerson.getCity());
            existingPerson.setZip(updatePerson.getZip());
            existingPerson.setPhone(updatePerson.getPhone());
            existingPerson.setEmail(updatePerson.getEmail());

            save(existingPerson);
        } else {
            updated = false;
        }
        return updated;
    }

    //TODO: need to write into file again after deletion.
    public void delete(Person deletePerson) {
//        List<Person> people = new ArrayList<Person>();
//
//        findAll().forEach(person -> {
//            if (!person.equals(deletePerson)) {
//                people.add(person);
//            }
//        });
//
//        try {
//            objectMapper.writeValue(new File(filePath), people);
//            return true;
//        } catch (IOException ex) {
//            log.error(ex.getMessage());
//            return false;
//        }

    }

    public boolean save(Person person) {
        List<Person> personList = new ArrayList<>(findAll());
        boolean saved = false;

        try {
            personList.add(person);
            objectMapper.writeValue(new File(filePath + "tempData.json"), personList);
            saved = true;
        } catch (IOException ex) {
            log.error(ex.getMessage());
        }

        return saved;
    }
}
