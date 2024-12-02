package com.safetynet.alerts.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.model.DataObject;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.MedicalRecordRepository;
import com.safetynet.alerts.repository.PersonRepository;
import com.safetynet.alerts.view.PersonInfoView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class PersonServiceTest {

    private static final String TEST_FILE_PATH = "src/test/resources/personService";
    private final ObjectMapper objectMapper = new ObjectMapper();

    @InjectMocks
    PersonService personService;

    @Mock
    PersonRepository personRepository;

    @Mock
    MedicalRecordRepository medicalRecordRepository;


    @BeforeEach
    void setUp() throws IOException {
        List<Person> personList = objectMapper.readValue(new File(TEST_FILE_PATH + "/testPersonSameName.json"), DataObject.class).getPersons();
        List<MedicalRecord> medicalRecordList = objectMapper.readValue(new File(TEST_FILE_PATH + "/testPersonSameName.json"), DataObject.class).getMedicalRecords();

        when(personRepository.findAll()).thenReturn(personList);
        when(medicalRecordRepository.findAll()).thenReturn(medicalRecordList);
    }

    @Test
    void findPersonInfo() throws IOException {
        List<PersonInfoView> matchingName = personService.findPersonInfo("John", "Boyd");

        assertNotNull(matchingName);
        assertEquals(2, matchingName.size());
        assertFalse(matchingName.isEmpty());

        List<PersonInfoView> firstNameMatchOnly = personService.findPersonInfo("John", "NoName");
        assertNotNull(firstNameMatchOnly);
        assertEquals(0, firstNameMatchOnly.size());

        List<PersonInfoView> lastNameMatchOnly = personService.findPersonInfo("John", "NoName");
        assertNotNull(lastNameMatchOnly);
        assertEquals(0, lastNameMatchOnly.size());

        //Expected an empty list for non-existent person info
        List<PersonInfoView> noMatchingName = personService.findPersonInfo("NoName", "NoName");
        assertNotNull(noMatchingName);
        assertTrue(noMatchingName.isEmpty());
    }

    @Test
    void findPersonInfo_same_name() throws IOException {
        List<PersonInfoView> infoViewList = personService.findPersonInfo("John", "Boyd");

        assertEquals(2, infoViewList.size());

        PersonInfoView firstPersonInfo = infoViewList.get(0);

        assertEquals("John Boyd", firstPersonInfo.getName());
        assertEquals("123 main St, MainCity, 00001", firstPersonInfo.getAddress());
        assertEquals("noJohnBoyd@email.com", firstPersonInfo.getEmail());
        assertEquals(40, firstPersonInfo.getAge());
        assertEquals(2, firstPersonInfo.getMedications().size());
        assertEquals(1, firstPersonInfo.getAllergies().size());

        PersonInfoView secondPersonInfo = infoViewList.get(1);

        assertEquals("John Boyd", secondPersonInfo.getName());
        assertEquals("1509 Culver St, Culver, 97451", secondPersonInfo.getAddress());
        assertEquals("jaboyd@email.com", secondPersonInfo.getEmail());
        assertEquals(0, secondPersonInfo.getMedications().size());
        assertEquals(1, secondPersonInfo.getAllergies().size());

    }

    @Test
    public void findPersonInfo_NoMedicalRecords() throws IOException {
        List<MedicalRecord> emptyMedicalRecordList = List.of();

        when(medicalRecordRepository.findAll()).thenReturn(emptyMedicalRecordList);

        List<PersonInfoView> results = personService.findPersonInfo("John", "Boyd");

        assertTrue(results.isEmpty(), "Expected no PersonInfoView for Jane Smith");
    }
}
