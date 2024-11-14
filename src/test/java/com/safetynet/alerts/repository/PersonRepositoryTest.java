package com.safetynet.alerts.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.model.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {PersonRepository.class})
public class PersonRepositoryTest {
    @Autowired
    private PersonRepository personRepository;

    private ObjectMapper objectMapper;

    private Person expectedUpdatePerson;
    private Person expectedFindPerson;
    private Person expectedNewPerson;

    private static final String TEST_FILE_PATH = "src/test/resources/personDir";

    @BeforeEach
    public void setUp() throws IOException {
        objectMapper = new ObjectMapper();

        expectedUpdatePerson = objectMapper.readValue(Paths.get(TEST_FILE_PATH + "/testUpdatePerson.json").toFile() , Person.class);
        expectedFindPerson = objectMapper.readValue(Paths.get(TEST_FILE_PATH + "/testFindPerson.json").toFile() , Person.class);
        expectedNewPerson = objectMapper.readValue(Paths.get(TEST_FILE_PATH + "/testNewPerson.json").toFile(), Person.class);
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
        var actual = personRepository.findByFirstAndLastName(expectedFindPerson);

        assertNotNull(actual);
        assertEquals(expectedFindPerson.getFirstName(), actual.getFirstName());
        assertEquals(expectedFindPerson.getLastName(), actual.getLastName());
        assertEquals(expectedFindPerson.getAddress(), actual.getAddress());
        assertEquals(expectedFindPerson.getCity(), actual.getCity());
        assertEquals(expectedFindPerson.getZip(), actual.getZip());
        assertEquals(expectedFindPerson.getEmail(), actual.getEmail());
    }

    @Test
    public void testUpdateExistingPerson() throws IOException {
        var nonExpectedJson = "{ \"firstName\":\"\", \"lastName\":\"\"}";
        var nonExpectedPerson = objectMapper.readValue(nonExpectedJson, Person.class);

        assertTrue(personRepository.updateExistingPerson(expectedUpdatePerson));
        assertFalse(personRepository.updateExistingPerson(nonExpectedPerson));

        var findExpectedPerson = personRepository.findByFirstAndLastName(expectedUpdatePerson);

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
        var expectedSizePersonList = 22;

        personRepository.delete(expectedUpdatePerson);

        var actualSizePersonList = personRepository.findAll().size();

        assertEquals(expectedSizePersonList, actualSizePersonList);
        assertEquals(expectedSizePersonList, personRepository.findAll().size());

        assertNull(personRepository.findByFirstAndLastName(expectedUpdatePerson));
    }

    @Test
    public void testSave() {
        personRepository.save(expectedNewPerson);

        var findExpectedPerson = personRepository.findByFirstAndLastName(expectedNewPerson);

        assertEquals("big", findExpectedPerson.getFirstName());
        assertEquals("head", findExpectedPerson.getLastName());
        assertEquals("czz", findExpectedPerson.getAddress());
        assertEquals("seoul", findExpectedPerson.getCity());
        assertEquals("00000", findExpectedPerson.getZip());
        assertEquals("000-000-0000", findExpectedPerson.getPhone());
        assertEquals("bighead@email.com", findExpectedPerson.getEmail());
    }
}
