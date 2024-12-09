package com.safetynet.alerts.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.view.ChildAlertResponse;
import com.safetynet.alerts.view.PersonInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class ChildAlertResponseServiceTest {

    static final String TEST_FILE_PATH = "src/test/resources";
    final ObjectMapper objectMapper = new ObjectMapper();

    @InjectMocks
    ChildAlertResponseService childAlertResponseService;
    @Mock
    PersonInfoService personInfoService;

    @BeforeEach
    void setUp() throws IOException {
        Map<String, PersonInfo> personInfoMap = objectMapper.readValue(new File(TEST_FILE_PATH + "/personService/testPersonInfoMap.json"), new TypeReference<Map<String, PersonInfo>>() {
        });

        when(personInfoService.getPersonInfoViewMap()).thenReturn(personInfoMap);
    }

    @Test
    void findChildAlert() {
        ChildAlertResponse childAlertResponse = childAlertResponseService.findChildAlert("1509 Culver St");

        assertNotNull(childAlertResponse);
        assertNotNull(childAlertResponse.getChildren());
        assertNotNull(childAlertResponse.getOtherPersons());

        assertEquals(2, childAlertResponse.getChildren().size());
        assertEquals("Tenley", childAlertResponse.getChildren().get(0).getFirstName());
        assertEquals("Boyd", childAlertResponse.getChildren().get(0).getLastName());
        assertEquals(12, childAlertResponse.getChildren().get(0).getAge());

        assertEquals("Roger", childAlertResponse.getChildren().get(1).getFirstName());
        assertEquals("Boyd", childAlertResponse.getChildren().get(1).getLastName());
        assertEquals(7, childAlertResponse.getChildren().get(1).getAge());

        assertEquals(3, childAlertResponse.getOtherPersons().size());
        assertEquals("John", childAlertResponse.getOtherPersons().get(0).getFirstName());
        assertEquals("Boyd", childAlertResponse.getOtherPersons().get(0).getLastName());
        assertEquals(40, childAlertResponse.getOtherPersons().get(0).getAge());
        assertEquals("1509 Culver St", childAlertResponse.getOtherPersons().get(0).getAddress());
    }

    @Test
    void findChildAlert_no_matching_children(){
        ChildAlertResponse childAlertResponse = childAlertResponseService.findChildAlert("29 15th St");

        assertNotNull(childAlertResponse);
    }
}