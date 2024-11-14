package com.safetynet.alerts.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.model.DataObject;
import com.safetynet.alerts.model.FireStation;
import com.safetynet.alerts.repository.FireStationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.io.File;
import java.io.IOException;
import java.util.List;

@WebMvcTest(FireStationController.class)
class FireStationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FireStationRepository  fireStationRepository;

    private String fireStationJsonList;
    private String newFireStationFile;
    private String editFireStationFile;

    private static final String TEST_FILE_PATH = "src/test/resources";

    @BeforeEach
    void setUp() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        List<FireStation> fireStationList = objectMapper.readValue(new File(TEST_FILE_PATH), DataObject.class).getFireStations();
    }

    @Test
    void getPeople() {
    }

    @Test
    void addFireStation() {
    }

    @Test
    void updateExistingFireStation() {
    }

    @Test
    void deleteExistingFireStation() {
    }
}