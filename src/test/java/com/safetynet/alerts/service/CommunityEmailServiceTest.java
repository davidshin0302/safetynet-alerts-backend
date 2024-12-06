package com.safetynet.alerts.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.model.DataObject;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class CommunityEmailServiceTest {
    private static final String TEST_FILE_PATH = "src/test/resources";
    private final ObjectMapper objectMapper = new ObjectMapper();
    private List<Person> personList;

    @InjectMocks
    CommunityEmailService communityEmailService;

    @Mock
    PersonRepository personRepository;

    @BeforeEach
    void setUp() throws IOException {
        personList = objectMapper.readValue(new File(TEST_FILE_PATH + "/testData.json"), DataObject.class).getPersons();

        when(personRepository.findAll()).thenReturn(personList);
    }

    @Test
    void findCommunityEmailsByCity() throws IOException {
        List<String> actualCommunityEmailList = communityEmailService.findCommunityEmailsByCity("Culver");
        List<String> expectedCommunityEmailList = objectMapper.readValue(new File(TEST_FILE_PATH + "/communityEmail/testExpectedCommunityEmail.json"), new TypeReference<List<String>>() {
        });

        assertNotNull(expectedCommunityEmailList);
        assertFalse(expectedCommunityEmailList.isEmpty());
        assertEquals(expectedCommunityEmailList.size(), actualCommunityEmailList.size());
    }

    @Test
    void findCommunityEmails_NewlyAddedPerson() {
        Person person = new Person("David", "Shin", "123 main st", "flushing", "11104", "123-456-9583", "david@email.com");
        List<String> expectedCommunityEmailList;
        List<Person> copyPersonList = new ArrayList<>(personList);

        copyPersonList.add(person);

        when(personRepository.findAll()).thenReturn(copyPersonList);

        expectedCommunityEmailList = communityEmailService.findCommunityEmailsByCity("flushing");

        assertNotNull(expectedCommunityEmailList);
        assertFalse(expectedCommunityEmailList.isEmpty());
        assertEquals(1, expectedCommunityEmailList.size());
        assertEquals("david@email.com", expectedCommunityEmailList.get(0));
    }

    @Test
    void findCommunityEmails_NonExistentCity(){
        List<String> expectedCommunityEmailList = communityEmailService.findCommunityEmailsByCity("noCity");

        assertNotNull(expectedCommunityEmailList);
        assertTrue(expectedCommunityEmailList.isEmpty());
    }
}