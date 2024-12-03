package com.safetynet.alerts.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.model.DataObject;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.MedicalRecordRepository;
import com.safetynet.alerts.repository.PersonRepository;
import com.safetynet.alerts.service.CommunityEmailService;
import com.safetynet.alerts.service.FireResponseService;
import com.safetynet.alerts.service.PersonService;
import com.safetynet.alerts.view.PersonInfoView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(AlertController.class)
class AlertControllerTest {

    private static final String TEST_FILE_PATH = "src/test/resources";
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @InjectMocks
    private AlertController alertController;
    @MockBean
    private PersonService personService;
    @MockBean
    private CommunityEmailService communityEmailService;
    @MockBean
    private FireResponseService fireResponseService;
    @Mock
    private PersonRepository personRepository;
    @Mock
    private MedicalRecordRepository medicalRecordRepository;

    @BeforeEach
    void setUp() throws IOException {
        List<Person> personList = objectMapper.readValue(new File(TEST_FILE_PATH + "/personService/testPersonSameName.json"), DataObject.class).getPersons();
        List<MedicalRecord> medicalRecordList = objectMapper.readValue(new File(TEST_FILE_PATH + "/personService/testPersonSameName.json"), DataObject.class).getMedicalRecords();

        when(personRepository.findAll()).thenReturn(personList);
        when(medicalRecordRepository.findAll()).thenReturn(medicalRecordList);
    }

    @Test
    void getPersonInfo() throws Exception {
        List<PersonInfoView> personInfoViewList = objectMapper.readValue(new File(TEST_FILE_PATH + "/personService/testExpectedMultiplePerson.json"), new TypeReference<List<PersonInfoView>>() {
        });

        when(personService.findPersonInfo(anyString(), anyString())).thenReturn(personInfoViewList);

        mockMvc.perform(get("/personInfo?firstName=John&lastName=Boyd"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void getPersonInfo_RuntimeException() throws Exception {
        when(personService.findPersonInfo(anyString(), anyString())).thenThrow(new RuntimeException("RuntimeException error"));

        mockMvc.perform(get("/personInfo?firstName=NoName&lastName=NoName"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void getCommunityEmail() throws Exception {
        List<String> communityEmailList = objectMapper.readValue(new File(TEST_FILE_PATH + "/CommunityEmail/testExpectedCommunityEmail.json"), new TypeReference<List<String>>() {
        });

        when(communityEmailService.findCommunityEmailsByCity(anyString())).thenReturn(communityEmailList);

        mockMvc.perform(get("/communityEmail?city=Culver"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(23)));
    }

    @Test
    void getCommunityEmail_RuntimeException() throws Exception {
        when(communityEmailService.findCommunityEmailsByCity(anyString())).thenThrow(new RuntimeException("RuntimeException error"));

        mockMvc.perform(get("/communityEmail?city=Culver"))
                .andExpect(status().isInternalServerError());
    }
}