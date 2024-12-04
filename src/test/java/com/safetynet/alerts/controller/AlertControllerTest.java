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
import com.safetynet.alerts.view.FireResponse;
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
import static org.hamcrest.Matchers.hasValue;
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
    void setAlertController_RuntimeException() throws Exception {
        when(personService.findPersonInfo(anyString(), anyString())).thenThrow(new RuntimeException("RuntimeException error"));
        when(communityEmailService.findCommunityEmailsByCity(anyString())).thenThrow(new RuntimeException("RuntimeException error"));
        when(fireResponseService.findFireResponse(anyString())).thenThrow(new RuntimeException("RuntimeException error"));

        //getPersonInfo_RuntimeException
        mockMvc.perform(get("/personInfo?firstName=NoName&lastName=NoName"))
                .andExpect(status().isInternalServerError());

        //getCommunityEmail_RuntimeException
        mockMvc.perform(get("/communityEmail?city=noCity"))
                .andExpect(status().isInternalServerError());

        //getFireResponse_RuntimeException
        mockMvc.perform(get("/fire?address=no address"))
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
    void getFireResponse() throws Exception {
        FireResponse fireResponse = objectMapper.readValue(new File(TEST_FILE_PATH + "/FireResponseService/testExpectedFireResponse.json"), FireResponse.class);
        when(fireResponseService.findFireResponse(anyString())).thenReturn(fireResponse);

        mockMvc.perform(get("/fire?address=1509 Culver St"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.address").value(fireResponse.getAddress()))
                .andExpect(jsonPath("$.fireStationNumber").value(fireResponse.getFireStationNumber()))
                .andExpect(jsonPath("$.residents", hasSize(5)));
    }
}