package com.safetynet.alerts.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.model.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {PersonRepository.class})
public class PersonRepositoryTest {
    private PersonRepository personRepository;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() throws IOException {
        objectMapper = new ObjectMapper();
        personRepository = new PersonRepository();
    }

    @Test
    public void testFindAll() throws IOException {
        var persons = personRepository.findAll();
        assertNotNull(persons);
        assertEquals(23, persons.size());

        Person person = persons.get(0);
        assertEquals("John", person.getFirstName());
        assertEquals("Boyd", person.getLastName());
        assertEquals("1509 Culver St", person.getAddress());
        assertEquals("Culver", person.getCity());
        assertEquals("97451", person.getZip());
        assertEquals("841-874-6512", person.getPhone());
        assertEquals("jaboyd@email.com", person.getEmail());
    }

    @Test
    public void testFindByFirstAndLastName() throws JsonProcessingException {
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

    @Test
    public void testUpdateExistingPerson() throws IOException {
        //Given
        var expectedJson = "{ \"firstName\":\"John\", \"lastName\":\"Boyd\", \"address\":\"123 Main St\", \"city\":\"Oakland\", \"zip\":\"11101\", \"phone\":\"123-456-7890\", \"email\":\"jaboyd@email.com\" }";
        var nonExpectedJson = "{ \"firstName\":\"\", \"lastName\":\"\"}";
        //When
        var expectedPerson = objectMapper.readValue(expectedJson, Person.class);
        var nonExpectedPerson = objectMapper.readValue(nonExpectedJson, Person.class);
        //Then

        assertTrue(personRepository.updateExistingPerson(expectedPerson));
        assertFalse(personRepository.updateExistingPerson(nonExpectedPerson));

        var findExpectedPerson = personRepository.findByFirstAndLastName(expectedPerson);

        assertEquals("John", findExpectedPerson.getFirstName());
        assertEquals("Boyd", findExpectedPerson.getLastName());
        assertEquals("123 Main St", findExpectedPerson.getAddress());
        assertEquals("Oakland", findExpectedPerson.getCity());
        assertEquals("11101", findExpectedPerson.getZip());
        assertEquals("123-456-7890", findExpectedPerson.getPhone());
        assertEquals("jaboyd@email.com", findExpectedPerson.getEmail());
    }

    @Test
    public void testDelete() throws IOException {
//        var expectedJson = "{ \"firstName\":\"John\", \"lastName\":\"Boyd\", \"address\":\"1509 Culver St\", \"city\":\"Culver\", \"zip\":\"97451\", \"phone\":\"841-874-6512\", \"email\":\"jaboyd@email.com\" }";
//        var expectedPerson = objectMapper.readValue(expectedJson, Person.class);
//        var expectedSizePersonList = 22;
//
//        personRepository.delete(expectedPerson);
//
//        var actualSizePersonList = personRepository.findAll().size();
//
//        assertEquals(expectedSizePersonList, actualSizePersonList);
    }
}
