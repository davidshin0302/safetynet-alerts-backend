package com.safetynet.alerts.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.model.DataObject;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.MedicalRecordRepository;
import com.safetynet.alerts.repository.PersonRepository;
import com.safetynet.alerts.view.PersonInfoView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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

@Disabled
@ExtendWith(SpringExtension.class)
public class PersonServiceTest {

    private static final String TEST_FILE_PATH = "src/test/resources";
    private final ObjectMapper objectMapper = new ObjectMapper();

    @InjectMocks
    PersonService personService;

    @Mock
    PersonRepository personRepository;

    @Mock
    MedicalRecordRepository medicalRecordRepository;

    @Mock
    Logger log;

    private List<MedicalRecord> medicalRecordList;
    private List<PersonInfoView> personInfoViewList;

    @BeforeEach
    void setUp() throws IOException {
        when(personRepository.findAll()).thenReturn(objectMapper.readValue(new File(TEST_FILE_PATH + "/personService/testPersonSameName.json"), DataObject.class).getPersons());
        when(medicalRecordRepository.findAll()).thenReturn(objectMapper.readValue(new File(TEST_FILE_PATH + "/personService/testPersonSameName.json"), DataObject.class).getMedicalRecords());

        List<Person> personList = personRepository.findAll();
        medicalRecordList = medicalRecordRepository.findAll();
    }

    @Test
    void findPersonInfo() throws IOException {
        when(medicalRecordRepository.findRecord("John", "Boyd")).thenReturn(medicalRecordList.get(0));
        personInfoViewList = personService.findPersonInfo("John", "Boyd");

        assertFalse(personInfoViewList.isEmpty());

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
    void findPersonInfo_same_name() {
        when(medicalRecordRepository.findRecord("John", "Boyd")).thenReturn(medicalRecordList.get(0));
        personInfoViewList = personService.findPersonInfo("John", "Boyd");

        PersonInfoView firstPersonInfo = personInfoViewList.get(0);

        assertEquals(2, personInfoViewList.size());
        assertEquals("John Boyd", firstPersonInfo.getName());
        assertEquals("1509 Culver St Culver, 97451",firstPersonInfo.getAddress());
        assertEquals("jaboyd@email.com", firstPersonInfo.getEmail());
        assertEquals(2, firstPersonInfo.getMedications().size());
        assertEquals(1, firstPersonInfo.getAllergies().size());

        //Test if personService returns all the record from same name.
        when(medicalRecordRepository.findRecord("John", "Boyd")).thenReturn(medicalRecordList.get(1));
        List<PersonInfoView> secondPersonInfoViewList = personService.findPersonInfo("John", "Boyd");
        PersonInfoView secondPersonInfo = secondPersonInfoViewList.get(1);

        assertEquals(2, personInfoViewList.size());
        assertEquals("John Boyd", secondPersonInfo.getName());
        assertEquals("123 main St MainCity, 00001",secondPersonInfo.getAddress());
        assertEquals("noJohnBoyd@email.com", secondPersonInfo.getEmail());
        assertEquals(0, secondPersonInfo.getMedications().size());
        assertEquals(1, secondPersonInfo.getAllergies().size());
    }

    @Test
    void findPersonInfo_when_medicalRecord_is_null() throws IOException {
        // Prepare the updated person list from test file
        List<Person> updatedPersonList = objectMapper.readValue(new File(TEST_FILE_PATH + "/personService/testPersonService.json"), DataObject.class).getPersons();
        assertEquals(24, updatedPersonList.size());

        // Simulate a null medical record for a specific person
        when(personRepository.findAll()).thenReturn(updatedPersonList);
        when(medicalRecordRepository.findRecord(anyString(), anyString())).thenReturn(null);

        personInfoViewList = personService.findPersonInfo("big", "head");
        assertTrue(personInfoViewList.isEmpty());
    }
}
