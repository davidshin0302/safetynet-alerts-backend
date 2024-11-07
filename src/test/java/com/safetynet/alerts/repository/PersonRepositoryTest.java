package com.safetynet.alerts.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.model.DataObject;
import com.safetynet.alerts.model.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {PersonRepository.class})
public class PersonRepositoryTest {

    PersonRepository personRepository;
    ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() throws IOException {
        objectMapper = new ObjectMapper();

        String filePath = "/data.json";
        /*Read the Json file into Memory: Load the data from data.json into a list of Person objects.
        Perform the deletion in Memory: Create a new list that excludes the target Person without modifiying the original list.
        get.Class().getResourceAsStream(): Looks for resources in the classpath, typically under the src/main/resources
         */
        InputStream inputStream = getClass().getResourceAsStream(filePath);
        List<Person> personList = objectMapper.readValue(inputStream, DataObject.class).getPersons();

        List<Person> testPersonList = new ArrayList<>(personList);
        personRepository = new PersonRepository(testPersonList);
    }

    @Test
    public void testFindAllBySize() {
        //GIVEN
        var expected = 23;
        //WHEN
        var actual = personRepository.findAll();
        //THEN
        assertEquals(23, actual.size());
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
    public void testDelete() throws IOException {
        var expectedJson = "{ \"firstName\":\"John\", \"lastName\":\"Boyd\", \"address\":\"1509 Culver St\", \"city\":\"Culver\", \"zip\":\"97451\", \"phone\":\"841-874-6512\", \"email\":\"jaboyd@email.com\" }";
        var expectedPerson = objectMapper.readValue(expectedJson, Person.class);
        var expectedSizePersonList = 22;

        personRepository.delete(expectedPerson);

        var actualSizePersonList = personRepository.findAll().size();

        assertEquals(expectedSizePersonList, actualSizePersonList);
    }
}
