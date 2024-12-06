package com.safetynet.alerts.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.model.DataObject;
import com.safetynet.alerts.model.FireStation;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.FireStationRepository;
import com.safetynet.alerts.repository.PersonRepository;
import com.safetynet.alerts.view.FireResponse;
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
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class FireResponseServiceTest {

    private static final String TEST_FILE_PATH = "src/test/resources";
    private final ObjectMapper objectMapper = new ObjectMapper();

    @InjectMocks
    FireResponseService fireResponseService;

    @Mock
    PersonService personService;

    @Mock
    PersonRepository personRepository;

    @Mock
    FireStationRepository fireStationRepository;

    @BeforeEach
    void setUp() throws IOException {
        List<Person> personList = objectMapper.readValue(new File(TEST_FILE_PATH + "/testData.json"), DataObject.class).getPersons();
        List<FireStation> fireStationList = objectMapper.readValue(new File(TEST_FILE_PATH + "/testData.json"), DataObject.class).getFireStations();
        List<PersonInfoView> personInfoViewList = objectMapper.readValue(new File(TEST_FILE_PATH + "/personService/testExpectedOnePerson.json"), new TypeReference<List<PersonInfoView>>() {
        });

        when(personRepository.findAll()).thenReturn(personList);
        when(fireStationRepository.findAll()).thenReturn(fireStationList);
        when(personService.findPersonInfo(anyString(), anyString())).thenReturn(personInfoViewList);
    }

    @Test
    void findFireResponse() throws IOException {
        FireResponse expectedFireResponse = objectMapper.readValue(new File(TEST_FILE_PATH + "/fireResponseService/testExpectedFireResponse.json"), FireResponse.class);
        String address = "1509 Culver St";
        FireResponse actualFireResponse = fireResponseService.findFireResponse(address);

        assertNotNull(actualFireResponse);
        assertEquals(actualFireResponse.getFireStationNumber(), expectedFireResponse.getFireStationNumber());

    }

    @Test
    void getFireResponse() {
        Map<String, FireResponse> fireResponseMap = fireResponseService.getFireResponse();

        assertNotNull(fireResponseMap);
        assertEquals(11, fireResponseMap.size());
        assertTrue(fireResponseMap.containsKey("748 Townings Dr"));
        assertTrue(fireResponseMap.containsKey("951 LoneTree Rd"));
    }

    @Test
    void findFireResponse_NonExistent_Address() {
        String address = "Wonder Land";

        FireResponse actualFireResponse = fireResponseService.findFireResponse(address);

        assertNotNull(actualFireResponse);
        assertNull((actualFireResponse.getFireStationNumber()));
        assertNull((actualFireResponse.getAddress()));
        assertNull((actualFireResponse.getResidents()));
    }
}