package com.safetynet.alerts.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.model.Person;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {PersonRepository.class})
public class PersonRepositoryTest {
    @Autowired
    private PersonRepository personRepository;

    @Test
    public void testFindALlBySize() {
        //GIVEN
        var expected = 23;
        //WHEN
        var actual = personRepository.findAll();
        //THEN
        assertEquals(23, actual.size());
    }

    @Test
    public void testFndByFirstAndLastName() throws JsonProcessingException {
        var objectMapper = new ObjectMapper();
        var expectedJson = "{ \"firstName\":\"John\", \"lastName\":\"Boyd\", \"address\":\"1509 Culver St\", \"city\":\"Culver\", \"zip\":\"97451\", \"phone\":\"841-874-6512\", \"email\":\"jaboyd@email.com\" }";

        var expectedPerson = objectMapper.readValue(expectedJson, Person.class);
        var actual = personRepository.findByFirstAndLastName(expectedPerson);

        assertNotNull(actual);
        assertEquals(expectedPerson.getFirstName(), actual.getFirstName());
        assertEquals(expectedPerson.getLastName(), actual.getLastName());
        assertEquals(expectedPerson.getAddress(), actual.getAddress());
        assertEquals(expectedPerson.getCity(), actual.getCity());
        assertEquals(expectedPerson.getZip(), actual.getZip());
        assertEquals(expectedPerson.getEmail(), actual.getEmail());
    }
}
