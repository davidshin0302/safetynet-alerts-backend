package com.safetynet.alerts.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.view.FireResponse;
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
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class PhoneAlertResponseServiceTest {
    static final String TEST_FILE_PATH = "src/test/resources/fireResponseService";
    final ObjectMapper objectMapper = new ObjectMapper();

    @InjectMocks
    PhoneAlertResponseService phoneAlertResponseService;
    @Mock
    FireResponseService fireResponseService;

    @BeforeEach
    void setUp() throws IOException {
        Map<String, FireResponse> fireResponseMap;
        fireResponseMap = objectMapper.readValue(new File(TEST_FILE_PATH + "/testExpectedFireResponseMap.json"), new TypeReference<Map<String, FireResponse>>() {
        });

        when(fireResponseService.getFireResponse()).thenReturn(fireResponseMap);
    }

    @Test
    void findPhoneAlert(){
        List<String> expectedPhoneAlertResponse = phoneAlertResponseService.findPhoneAlert("3");

        assertNotNull(expectedPhoneAlertResponse);
        assertEquals(7, expectedPhoneAlertResponse.size());
        assertEquals("841-874-6741", expectedPhoneAlertResponse.get(0));
        assertEquals("841-874-6544", expectedPhoneAlertResponse.get(1));
        assertEquals("841-874-6874", expectedPhoneAlertResponse.get(2));
        assertEquals("841-874-6512", expectedPhoneAlertResponse.get(3));
        assertEquals("841-874-6513", expectedPhoneAlertResponse.get(4));
        assertEquals("841-874-8888", expectedPhoneAlertResponse.get(5));
        assertEquals("841-874-9888", expectedPhoneAlertResponse.get(6));
    }
}