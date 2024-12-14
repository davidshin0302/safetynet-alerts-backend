package com.safetynet.alerts.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.view.FireResponse;
import com.safetynet.alerts.view.FireStationPersonnel;
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
class FireStationPersonnelServiceTest {
    private static final String TEST_FILE_PATH = "src/test/resources/fireResponseService";
    private final ObjectMapper objectMapper = new ObjectMapper();

    @InjectMocks
    FireStationPersonnelService fireStationPersonnelService;

    @Mock
    FireResponseService fireResponseService;

    @BeforeEach
    void setUp() throws IOException {
        Map<String, FireResponse> fireResponseMap = objectMapper.readValue(new File(TEST_FILE_PATH + "/testExpectedFireResponseMap.json"), new TypeReference<Map<String, FireResponse>>() {
        });

        when(fireResponseService.getFireResponse()).thenReturn(fireResponseMap);
    }

    @Test
    void findFireStationPersonnel() {
        FireStationPersonnel fireStationPersonnel = fireStationPersonnelService.findFireStationPersonnel("3");

        assertNotNull(fireStationPersonnel);
        assertEquals("3", fireStationPersonnel.getStationNumber());
        assertEquals(11, fireStationPersonnel.getOtherPersonInfoList().size());
        assertEquals(8, fireStationPersonnel.getAdultCount());
        assertEquals(3, fireStationPersonnel.getChildCount());
    }

    @Test
    void findFireStationPersonnel_no_matching_station() {
        FireStationPersonnel fireStationPersonnel = fireStationPersonnelService.findFireStationPersonnel("999");

        assertNotNull(fireStationPersonnel);
        assertEquals("999", fireStationPersonnel.getStationNumber());
        assertEquals(0, fireStationPersonnel.getOtherPersonInfoList().size());
        assertEquals(0, fireStationPersonnel.getAdultCount());
        assertEquals(0, fireStationPersonnel.getChildCount());
    }

}