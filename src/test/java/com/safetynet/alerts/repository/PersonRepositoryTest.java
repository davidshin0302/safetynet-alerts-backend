package com.safetynet.alerts.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.model.DataObject;
import com.safetynet.alerts.model.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(SpringExtension.class)
public class PersonRepositoryTest {
    @MockBean
    private PersonRepository personRepository;

    private ObjectMapper objectMapper = new ObjectMapper();

    private static final String TEST_FILE_PATH = "src/test/resources";

    @BeforeEach
    public void setUp() throws IOException {
        when(personRepository.findAll()).thenReturn(objectMapper.readValue(new File(TEST_FILE_PATH + "/testData.json"), DataObject.class).getPersons());
    }

    @Test
    public void testFindAll() {
        var personList = personRepository.findAll();

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
    public void testFindByFirstAndLastName() throws IOException {
        var filePath = new String(Files.readAllBytes(Paths.get(TEST_FILE_PATH + "/personDir/testEditPerson.json")));
        var actual = objectMapper.readValue(filePath, Person.class);

        when(personRepository.findByFirstAndLastName(any(Person.class))).thenReturn(actual);

        var expectedFindPerson = personRepository.findByFirstAndLastName(personRepository.findAll().get(0));


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
        var FindPersonFilePath = new String(Files.readAllBytes(Paths.get(TEST_FILE_PATH + "/personDir/testFindPerson.json")));
        var expectedFindPerson = objectMapper.readValue(FindPersonFilePath, Person.class);

        when(personRepository.updateExistingPerson(expectedFindPerson)).thenReturn(true);
        assertTrue(personRepository.updateExistingPerson(expectedFindPerson));

        var nonExpectedJson = "{ \"firstName\":\"\", \"lastName\":\"\"}";
        var nonExpectedPerson = objectMapper.readValue(nonExpectedJson, Person.class);

        when(personRepository.updateExistingPerson(nonExpectedPerson)).thenReturn(false);
        assertFalse(personRepository.updateExistingPerson(nonExpectedPerson));

        var EditPersonFilePath = new String(Files.readAllBytes(Paths.get(TEST_FILE_PATH + "/personDir/testUpdatePerson.json")));
        var expectedUpdatePerson = objectMapper.readValue(EditPersonFilePath, Person.class);

        when(personRepository.findByFirstAndLastName(any(Person.class))).thenReturn(expectedUpdatePerson);
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
        var personList = new ArrayList<>(personRepository.findAll());
        var personToDelete = personList.get(0);

        when(personRepository.delete(personToDelete)).thenReturn(true);

        personList.remove(personToDelete);

        when(personRepository.findAll()).thenReturn(personList);

        assertTrue(personRepository.delete(personToDelete));
        assertEquals(personList.size(), personRepository.findAll().size());
    }

    @Test
    public void testSave() throws IOException {
        var personList = new ArrayList<>(personRepository.findAll());
        var filePath = new String(Files.readAllBytes(Paths.get(TEST_FILE_PATH + "/personDir/testNewPerson.json")));
        var newPerson = objectMapper.readValue(filePath, Person.class);

        when(personRepository.save(newPerson)).thenReturn(true);
        personList.add(newPerson);

        when(personRepository.findAll()).thenReturn(personList);
        assertTrue(personRepository.save(newPerson));
        assertTrue(personRepository.findAll().contains(newPerson));
        assertEquals(personList.size(), personRepository.findAll().size());
    }
}
