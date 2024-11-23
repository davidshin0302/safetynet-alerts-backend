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
import org.slf4j.Logger;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;


@ExtendWith(SpringExtension.class)
public class PersonServiceTest {

    private static final String TEST_FILE_PATH = "src/test/resources/testData.json";
    private static final String TEST_PERSON_SERVICE_FILE_PATH = "src/test/resources/personService/testPersonService.json";
    private final ObjectMapper objectMapper = new ObjectMapper();

    @InjectMocks
    PersonService personService;

    @Mock
    PersonRepository personRepository;

    @Mock
    MedicalRecordRepository medicalRecordRepository;

    @Mock
    Logger log;

    private List<Person> personList;
    private List<MedicalRecord> medicalRecordList;
    private List<PersonInfoView> personInfoViewList;

    @BeforeEach
    void setUp() throws IOException {

        when(personRepository.findAll()).thenReturn(objectMapper.readValue(new File(TEST_FILE_PATH), DataObject.class).getPersons());
        when(medicalRecordRepository.findAll()).thenReturn(objectMapper.readValue(new File(TEST_FILE_PATH), DataObject.class).getMedicalRecords());

        personList = personRepository.findAll();
        medicalRecordList = medicalRecordRepository.findAll();
    }

    @Test
    public void findPersonInfo() throws IOException {
        when(medicalRecordRepository.findRecord("John", "Boyd")).thenReturn(medicalRecordList.get(0));
        personInfoViewList = personService.findPersonInfo("John", "Boyd");

        assertFalse(personInfoViewList.isEmpty());
        assertEquals(1, personInfoViewList.size());

        //First name match but last name wrong.
        personInfoViewList = personService.findPersonInfo("John", "NoExist");
        assertTrue(personInfoViewList.isEmpty());

        //First name wrong but last name match
        personInfoViewList = personService.findPersonInfo("NoExist", "Boyd");
        assertTrue(personInfoViewList.isEmpty());

        //Both first name and last name no match.
        personInfoViewList = personService.findPersonInfo("NoExist", "NoExist");
        assertTrue(personInfoViewList.isEmpty());
    }

    @Test
    void findPersonInfo_when_medicalRecord_is_null() throws IOException {
        // Prepare the updated person list from test file
        List<Person> updatedPersonList = objectMapper.readValue(new File(TEST_PERSON_SERVICE_FILE_PATH), DataObject.class).getPersons();
        assertEquals(24, updatedPersonList.size());

        // Simulate a null medical record for a specific person
        when(personRepository.findAll()).thenReturn(updatedPersonList);
        when(medicalRecordRepository.findRecord(anyString(), anyString())).thenReturn(null);

        personInfoViewList = personService.findPersonInfo("big", "head");
        assertTrue(personInfoViewList.isEmpty());
    }
}
