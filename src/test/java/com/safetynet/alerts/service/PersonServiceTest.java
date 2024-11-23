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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class PersonServiceTest {

    private static final String TEST_FILE_PATH = "src/test/resources/testData.json";
    private final ObjectMapper objectMapper = new ObjectMapper();

    PersonService personService;

    @MockBean
    PersonRepository personRepository;

    @MockBean
    MedicalRecordRepository medicalRecordRepository;

    private List<Person> personList;
    private List<MedicalRecord> medicalRecordList;

    @BeforeEach
    void setUp() throws IOException {
        personService = new PersonService();

        when(personRepository.findAll()).thenReturn(objectMapper.readValue(new File(TEST_FILE_PATH), DataObject.class).getPersons());
        when(medicalRecordRepository.findAll()).thenReturn(objectMapper.readValue(new File(TEST_FILE_PATH), DataObject.class).getMedicalRecords());

        personList = personRepository.findAll();
        medicalRecordList = medicalRecordRepository.findAll();
    }

    @Test
    public void findPersonInfo() {
        when(personRepository.findPerson(any(String.class), any(String.class))).thenReturn(personList.get(0));
        when(medicalRecordRepository.findRecord(any(String.class), any(String.class))).thenReturn(medicalRecordList.get(0));

        Person person = personRepository.findPerson("John", "Boyd");
        MedicalRecord medicalRecord = medicalRecordRepository.findRecord("John", "Boyd");

        List<PersonInfoView> personInfoViewList = personService.findPersonInfo("John", "Boyd");

        assertFalse(personInfoViewList.isEmpty());
    }
}
