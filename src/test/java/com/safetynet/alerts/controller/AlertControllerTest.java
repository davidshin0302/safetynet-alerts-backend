package com.safetynet.alerts.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.model.DataObject;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.MedicalRecordRepository;
import com.safetynet.alerts.repository.PersonRepository;
import com.safetynet.alerts.service.*;
import com.safetynet.alerts.view.*;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyList;
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
    private PersonInfoService personInfoService;
    @MockBean
    private CommunityEmailService communityEmailService;
    @MockBean
    private FireResponseService fireResponseService;
    @MockBean
    private FloodResponseService floodResponseService;
    @MockBean
    private ChildAlertResponseService childAlertResponseService;
    @MockBean
    private PhoneAlertResponseService phoneAlertResponseService;
    @MockBean
    private FireStationPersonnelService fireStationPersonnelService;
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
        List<PersonInfo> personInfoList = objectMapper.readValue(new File(TEST_FILE_PATH + "/personService/testExpectedMultiplePerson.json"), new TypeReference<List<PersonInfo>>() {
        });

        when(personInfoService.findPersonInfo(anyString(), anyString())).thenReturn(personInfoList);

        mockMvc.perform(get("/personInfo?firstName=John&lastName=Boyd"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void setAlertController_RuntimeException() throws Exception {
        when(personInfoService.findPersonInfo(anyString(), anyString())).thenThrow(new RuntimeException("RuntimeException error"));
        when(communityEmailService.findCommunityEmailsByCity(anyString())).thenThrow(new RuntimeException("RuntimeException error"));
        when(fireResponseService.findFireResponse(anyString())).thenThrow(new RuntimeException("RuntimeException error"));
        when(floodResponseService.findFloodResponse(anyList())).thenThrow(new RuntimeException("RuntimeException error"));
        when(childAlertResponseService.findChildAlert(anyString())).thenThrow(new RuntimeException("RuntimeException error"));
        when(phoneAlertResponseService.findPhoneAlert(anyString())).thenThrow(new RuntimeException("RuntimeException error"));
        when(fireStationPersonnelService.findFireStationPersonnel(anyString())).thenThrow(new RuntimeException("RuntimeException error"));

        //getPersonInfo_RuntimeException
        mockMvc.perform(get("/personInfo?firstName=NoName&lastName=NoName"))
                .andExpect(status().isInternalServerError());

        //getCommunityEmail_RuntimeException
        mockMvc.perform(get("/communityEmail?city=noCity"))
                .andExpect(status().isInternalServerError());

        //getFireResponse_RuntimeException
        mockMvc.perform(get("/fire?address=no address"))
                .andExpect(status().isInternalServerError());

        //getFloodResponse
        mockMvc.perform(get("/flood/stations?stations={}"))
                .andExpect(status().isInternalServerError());

        //getChildAlertResponse
        mockMvc.perform(get("/childAlert?address={}"))
                .andExpect(status().isInternalServerError());

        //getPhoneAlertResponse
        mockMvc.perform(get("/phoneAlert?firestation=={}"))
                .andExpect(status().isInternalServerError());
        //getFireStationPersonnel
        mockMvc.perform(get("/firestation?stationNumber={}"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void getCommunityEmail() throws Exception {
        List<String> communityEmailList = objectMapper.readValue(new File(TEST_FILE_PATH + "/communityEmail/testExpectedCommunityEmail.json"), new TypeReference<List<String>>() {
        });

        when(communityEmailService.findCommunityEmailsByCity(anyString())).thenReturn(communityEmailList);

        mockMvc.perform(get("/communityEmail?city=Culver"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(23)));
    }

    @Test
    void getFireResponse() throws Exception {
        FireResponse fireResponse = objectMapper.readValue(new File(TEST_FILE_PATH + "/fireResponseService/testExpectedFireResponse.json"), FireResponse.class);
        when(fireResponseService.findFireResponse(anyString())).thenReturn(fireResponse);

        mockMvc.perform(get("/fire?address=1509 Culver St"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.address").value(fireResponse.getAddress()))
                .andExpect(jsonPath("$.fireStationNumber").value(fireResponse.getFireStationNumber()))
                .andExpect(jsonPath("$.residents", hasSize(5)));
    }

    @Test
    void getFloodResponse() throws Exception {
        Map<String, List<FloodResponse>> floodResponseMap = objectMapper.readValue(new File(TEST_FILE_PATH + "/floodResponseService/testExpectedFloodResponse.json"), new TypeReference<Map<String, List<FloodResponse>>>() {
        });
        when(floodResponseService.findFloodResponse(anyList())).thenReturn(floodResponseMap);

        mockMvc.perform(get("/flood/stations?stations=1,2,3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stations", hasSize(3)))
                .andExpect(jsonPath("$.stations[0].households", hasSize(2)))
                .andExpect(jsonPath("$.stations[1].households", hasSize(2)))
                .andExpect(jsonPath("$.stations[2].households", hasSize(3)));
    }

    @Test
    void getChildAlert() throws Exception {
        ChildAlertResponse childAlertResponseMap = objectMapper.readValue(new File(TEST_FILE_PATH + "/childAlert/testExpectedChildAlert.json"), ChildAlertResponse.class);

        when(childAlertResponseService.findChildAlert(anyString())).thenReturn(childAlertResponseMap);

        mockMvc.perform(get("/childAlert?address=947 E. Rose Dr"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.children", hasSize(1)))
                .andExpect(jsonPath("$.children[0].firstName").value("Kendrik"))
                .andExpect(jsonPath("$.children[0].lastName").value("Stelzer"))
                .andExpect(jsonPath("$.children[0].age").value(10))

                .andExpect(jsonPath("$.otherPersons", hasSize(2)))
                .andExpect(jsonPath("$.otherPersons[1].age").value(49))
                .andExpect(jsonPath("$.otherPersons[1].firstName").value("Brian"))
                .andExpect(jsonPath("$.otherPersons[1].lastName").value("Stelzer"))
                .andExpect(jsonPath("$.otherPersons[1].address").value("947 E. Rose Dr"))

                .andExpect(jsonPath("$.otherPersons[0].age").value(44))
                .andExpect(jsonPath("$.otherPersons[0].firstName").value("Shawna"))
                .andExpect(jsonPath("$.otherPersons[0].lastName").value("Stelzer"))
                .andExpect(jsonPath("$.otherPersons[0].address").value("947 E. Rose Dr"));
    }

    @Test
    void getChildAlert_no_matching_children() throws Exception {
        ChildAlertResponse childAlertResponse = new ChildAlertResponse();
        childAlertResponse.setChildren(new ArrayList<>());
        childAlertResponse.setOtherPersons(new ArrayList<>());

        when(childAlertResponseService.findChildAlert(anyString())).thenReturn(childAlertResponse);

        mockMvc.perform(get("/childAlert?address=wrong address"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(""));
    }

    @Test
    void getPhoneAlertResponse() throws Exception {
        List<String> expectedPhoneNumbers = new ArrayList<>();
        expectedPhoneNumbers.add("841-874-6544");
        expectedPhoneNumbers.add("841-874-6741");


        when(phoneAlertResponseService.findPhoneAlert(anyString())).thenReturn(expectedPhoneNumbers);

        mockMvc.perform(get("/phoneAlert?firestation=3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0]").value("841-874-6544"))
                .andExpect(jsonPath("$[1]").value("841-874-6741"));
    }

    @Test
    void getPhoneAlert_no_station() throws Exception {
        List<String> expectedPhoneNumbers = new ArrayList<>();

        when(phoneAlertResponseService.findPhoneAlert(anyString())).thenReturn(expectedPhoneNumbers);

        mockMvc.perform(get("/phoneAlert?firestation=999"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void getFireStationPersonnel() throws Exception {
        FireStationPersonnel fireStationPersonnel = objectMapper.readValue(new File(TEST_FILE_PATH + "/firestationDir/testExpectedFirePersonnel.json"), FireStationPersonnel.class);

        when(fireStationPersonnelService.findFireStationPersonnel(anyString())).thenReturn(fireStationPersonnel);

        mockMvc.perform(get("/firestation?stationNumber=3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stationNumber").value("3"))
                .andExpect(jsonPath("$.otherPersonInfoList", hasSize(11)))
                .andExpect(jsonPath("$.adultCount").value(8))
                .andExpect(jsonPath("$.childCount").value(3));
    }

    @Test
    void getFireStationPersonnel_Non_StationNumber() throws Exception {
        FireStationPersonnel fireStationPersonnel = FireStationPersonnel.builder()
                .stationNumber("999")
                .otherPersonInfoList(new ArrayList<>())
                .adultCount(0)
                .childCount(0)
                .build();

        when(fireStationPersonnelService.findFireStationPersonnel(anyString())).thenReturn(fireStationPersonnel);

        mockMvc.perform(get("/firestation?stationNumber=3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stationNumber").value("999"))
                .andExpect(jsonPath("$.otherPersonInfoList", hasSize(0)))
                .andExpect(jsonPath("$.adultCount").value(0))
                .andExpect(jsonPath("$.childCount").value(0));
    }
}