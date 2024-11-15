package com.safetynet.alerts.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.model.DataObject;
import com.safetynet.alerts.repository.FireStationRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.io.File;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(FireStationController.class)
class FireStationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FireStationRepository fireStationRepository;

    @InjectMocks
    private FireStationController fireStationController;

    private ObjectMapper objectMapper = new ObjectMapper();

    private static final String TEST_FILE_PATH = "src/test/resources";

    @Test
    void testGetFireStation() throws Exception {
        // Return List of Fire Stations.
        when(fireStationRepository.findAll()).thenReturn(objectMapper.readValue(new File(TEST_FILE_PATH + "/testData.json"), DataObject.class).getFireStations());

        mockMvc.perform(get("/firestation")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(13)));
    }

    @Test
    public void testGetAllFireStationException() throws Exception {
        when(fireStationRepository.findAll()).thenThrow(new RuntimeException("RuntimeException error"));

        mockMvc.perform(get("/firestation"))
                .andExpect(status().isInternalServerError());  // Expecting HTTP status 500 Internal Server Error
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