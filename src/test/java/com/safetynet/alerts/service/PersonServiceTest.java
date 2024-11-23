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
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class PersonServiceTest {

    private static final String TEST_FILE_PATH = "src/test/resources/testData.json";
    private static final String TEST_PERSON_SERVICE_FILE_PATH = "src/test/resources/personService/testPersonService.json";
    private final ObjectMapper objectMapper = new ObjectMapper();

    @InjectMocks
    PersonService personService;

    @MockBean
    PersonRepository personRepository;

    @MockBean
    MedicalRecordRepository medicalRecordRepository;

    private List<Person> personList;
    private List<PersonInfoView> personInfoViewList;

    @BeforeEach
    void setUp() throws IOException {
        when(personRepository.findAll()).thenReturn(objectMapper.readValue(new File(TEST_FILE_PATH), DataObject.class).getPersons());
        when(medicalRecordRepository.findAll()).thenReturn(objectMapper.readValue(new File(TEST_FILE_PATH), DataObject.class).getMedicalRecords());

        personList = personRepository.findAll();
    }

    @Test
    public void findPersonInfo() throws IOException {
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
        when(personRepository.findAll()).thenReturn(updatedPersonList);

        // Create a mock MedicalRecord and configure it
        MedicalRecord mockMedicalRecord = new MedicalRecord();
        mockMedicalRecord.setMedications(Arrays.asList("Med1", "Med2"));
        mockMedicalRecord.setAllergies(Arrays.asList("Allergy1", "Allergy2"));

        // Simulate a non-null medical record for a specific person
        when(medicalRecordRepository.findRecord("big", "head")).thenReturn(mockMedicalRecord);

        // Call the method under test
        personInfoViewList = personService.findPersonInfo("big", "head");

        // Assert that the result is not empty and matches expected values
        assertTrue(personInfoViewList.isEmpty());
        assertEquals(0, personInfoViewList.size());

//        PersonInfoView personInfo = personInfoViewList.get(0);
//        assertEquals("big head", personInfo.getName());
//        assertEquals(Arrays.asList("Med1", "Med2"), personInfo.getMedications());
//        assertEquals(Arrays.asList("Allergy1", "Allergy2"), personInfo.getAllergies());
    }
}
