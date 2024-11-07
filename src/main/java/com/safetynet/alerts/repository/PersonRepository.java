package com.safetynet.alerts.repository;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.model.DataObject;
import com.safetynet.alerts.model.Person;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Repository
@Slf4j
public class PersonRepository {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String filePath = "src/main/resources/";
    private List<Person> personList = new ArrayList<>();

    public PersonRepository(List<Person> personList) {
        this.personList = personList;
    }

    public List<Person> findAll() {
        try {
            return objectMapper.readValue(new File(filePath + "data.json"), DataObject.class).getPersons();
        } catch (IOException ex) {
            log.error(ex.getMessage());
        }

        return List.of();
    }

    public Person findByFirstAndLastName(Person findPerson) {
        return findAll().stream()
                .filter(existingPerson -> existingPerson.equals(findPerson))
                .findFirst()
                .orElse(null);
    }

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
        boolean saved = true;

        try {
            personList.add(person);
            objectMapper.writeValue(new File(filePath + "tempData.json"), personList);
        } catch (IOException ex) {
            saved = false;
            log.error(ex.getMessage());
        }

        return saved;
    }
}
