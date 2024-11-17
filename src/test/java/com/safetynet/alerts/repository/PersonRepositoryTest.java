package com.safetynet.alerts.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.model.DataObject;
import com.safetynet.alerts.model.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
public class PersonRepositoryTest {
    private PersonRepository personRepository;
    private ObjectMapper objectMapper = new ObjectMapper();
    private List<Person> personList;

    private static final String TEST_FILE_PATH = "src/test/resources";

    @BeforeEach
    public void setUp() throws IOException {
        personRepository = new PersonRepository();
        personList = objectMapper.readValue(new File(TEST_FILE_PATH + "/testData.json"), DataObject.class).getPersons();
    }

    @Test
    void testFindAll() {
        assertNotNull(personList);
        assertEquals(23, personList.size());

        var person = personList.get(0);
        assertEquals("John", person.getFirstName());
        assertEquals("Boyd", person.getLastName());
        assertEquals("1509 Culver St", person.getAddress());
        assertEquals("Culver", person.getCity());
        assertEquals("97451", person.getZip());
        assertEquals("841-874-6512", person.getPhone());
        assertEquals("jaboyd@email.com", person.getEmail());
    }

    @Test
    void testFindByFirstAndLastName() throws IOException {
        var actualJson = "{ \"firstName\":\"Tessa\", \"lastName\":\"Carman\", \"address\":\"834 Binoc Ave\", \"city\":\"Culver\", \"zip\":\"97451\", \"phone\":\"841-874-6512\", \"email\":\"tenz@email.com\" }";
        var actual = objectMapper.readValue(actualJson, Person.class);

        var expectedFindPerson = personRepository.findByFirstAndLastName(actual);


        assertNotNull(actual);
        assertEquals(expectedFindPerson.getFirstName(), actual.getFirstName());
        assertEquals(expectedFindPerson.getLastName(), actual.getLastName());
        assertEquals(expectedFindPerson.getAddress(), actual.getAddress());
        assertEquals(expectedFindPerson.getCity(), actual.getCity());
        assertEquals(expectedFindPerson.getZip(), actual.getZip());
        assertEquals(expectedFindPerson.getEmail(), actual.getEmail());
    }

    @Test
    void testUpdateExistingPerson() throws IOException {
        var findPersonFilePath = "{ \"firstName\":\"John\", \"lastName\":\"Boyd\", \"address\":\"1509 Culver St\", \"city\":\"Culver\", \"zip\":\"97451\", \"phone\":\"841-874-6512\", \"email\":\"jaboyd@email.com\" }";
        var nonExpectedJson = "{ \"firstName\":\"\", \"lastName\":\"\"}";

        var expectedFindPerson = objectMapper.readValue(findPersonFilePath, Person.class);
        var nonExpectedPerson = objectMapper.readValue(nonExpectedJson, Person.class);


        assertTrue(personRepository.updateExistingPerson(expectedFindPerson));
        assertFalse(personRepository.updateExistingPerson(nonExpectedPerson));
    }

    @Test
    void testUpdateExistingPerson_actual_data() throws IOException {
        var findPersonFilePath = "{ \"firstName\":\"John\", \"lastName\":\"Boyd\", \"address\":\"1509 Culver St\", \"city\":\"Culver\", \"zip\":\"97451\", \"phone\":\"841-874-6512\", \"email\":\"jaboyd@email.com\" }";
        var expectedFindPerson = objectMapper.readValue(findPersonFilePath, Person.class);

        expectedFindPerson.setAddress("123 Main St");
        expectedFindPerson.setCity("Oakland");
        expectedFindPerson.setZip("11101");
        expectedFindPerson.setPhone("123-456-7890");
        expectedFindPerson.setEmail("jaboyd@email.com");

        assertEquals("John", expectedFindPerson.getFirstName());
        assertEquals("Boyd", expectedFindPerson.getLastName());
        assertEquals("123 Main St", expectedFindPerson.getAddress());
        assertEquals("Oakland", expectedFindPerson.getCity());
        assertEquals("11101", expectedFindPerson.getZip());
        assertEquals("123-456-7890", expectedFindPerson.getPhone());
        assertEquals("jaboyd@email.com", expectedFindPerson.getEmail());
    }

    @Test
    void testDelete() throws IOException {
        var newPersonJson = "{ \"firstName\":\"big\", \"lastName\":\"head\", \"address\":\"czz\", \"city\":\"seoul\", \"zip\":\"00000\", \"phone\":\"000-000-0000\", \"email\":\"bighead@email.com\" }";
        var person = objectMapper.readValue(newPersonJson, Person.class);

        personRepository.save(person);

        assertTrue(personRepository.delete(person));
        assertEquals(personList.size(), personRepository.findAll().size());
    }

    @Test
    void testDeletePersonWhenPersonIsNull() {
        var result = personRepository.delete(null);

        assertFalse(result, "Unable to find the person");
    }
}
