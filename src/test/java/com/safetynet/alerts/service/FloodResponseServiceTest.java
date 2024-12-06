package com.safetynet.alerts.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.view.FireResponse;
import com.safetynet.alerts.view.FloodResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class FloodResponseServiceTest {

    static final String TEST_FILE_PATH = "src/test/resources";
    final ObjectMapper objectMapper = new ObjectMapper();

    @InjectMocks
    FloodResponseService floodResponseService;
    @Mock
    FireResponseService fireResponseService;

    @BeforeEach
    void setUp() throws IOException {
        Map<String, FireResponse> floodResponseMap = objectMapper.readValue(new File(TEST_FILE_PATH + "/floodResponseService/testExpectedFireResponse2.json"), new TypeReference<Map<String, FireResponse>>() {
        });
        when(fireResponseService.getFireResponse()).thenReturn(floodResponseMap);
    }

    @Test
    void findFloodResponse(){
        String key = "stations";
        List<String> stations = List.of("1","2","3");
        Map<String, List<FloodResponse>> expectedFloodResponse = floodResponseService.findFloodResponse(stations);

        assertNotNull(expectedFloodResponse);
        assertEquals(3, expectedFloodResponse.get(key).size());
        assertEquals(2, expectedFloodResponse.get(key).get(0).getHouseholds().size());
        assertEquals(2, expectedFloodResponse.get(key).get(1).getHouseholds().size());
        assertEquals(3, expectedFloodResponse.get(key).get(2).getHouseholds().size());

        assertEquals("1", expectedFloodResponse.get(key).get(0).getStationNumber());
        assertEquals("2", expectedFloodResponse.get(key).get(1).getStationNumber());
        assertEquals("3", expectedFloodResponse.get(key).get(2).getStationNumber());
    }

    @Test
    void findFloodResponse_test_fireResponseEmpty(){
        when(fireResponseService.getFireResponse()).thenReturn(new HashMap<>());

        String key = "stations";
        List<String> station = List.of("1");
        Map<String, List<FloodResponse>> expectedFloodResponse = floodResponseService.findFloodResponse(station);

        assertTrue(expectedFloodResponse.get(key).isEmpty());
    }

}