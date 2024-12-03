package com.safetynet.alerts.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.model.DataObject;
import com.safetynet.alerts.model.FireStation;
import com.safetynet.alerts.repository.FireStationRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.io.File;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(FireStationController.class)
class FireStationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FireStationRepository fireStationRepository;

    @Mock
    private Logger log;

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
    void addFireStation() throws Exception {
        FireStation fireStation = new FireStation();
        fireStation.setAddress("85 central st ");
        fireStation.setStation("109");

        String newFireStation = objectMapper.writeValueAsString(fireStation);

        when(fireStationRepository.save(fireStation)).thenReturn(true);
//        when(fireStationRepository.findFireStation(fireStation.getAddress())).thenReturn(fireStation);

        //Verify new firestation is saved and return with 201 code.
        mockMvc.perform(post("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newFireStation))
                .andExpect(status().isConflict())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string("FireStation could not be saved."));
    }

    @Test
    void addFireStation_conflict() throws Exception {
        FireStation fireStation = new FireStation();
        fireStation.setAddress("invalid");
        fireStation.setStation("data");

        when(fireStationRepository.save(any(FireStation.class))).thenReturn(false);

        mockMvc.perform(post("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(fireStation)))
                .andExpect(status().isConflict());
    }


    @Test
    void updateExistingFireStation() throws Exception {
        String fireStationJson = "{ \"address\":\"1509 Culver St\", \"station\":\"3\" }";
        FireStation fireStation = objectMapper.readValue(fireStationJson, FireStation.class);

        when(fireStationRepository.updateExistingFireStationNumber(fireStation)).thenReturn(true);
        when(fireStationRepository.findFireStation(fireStation.getAddress())).thenReturn(fireStation);

        mockMvc.perform(put("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(fireStation)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.address").value(fireStation.getAddress()))
                .andExpect(jsonPath("$.station").value(fireStation.getStation()));
    }

    @Test
    void updateExistingFireStation_notFound() throws Exception {
        FireStation fireStation = new FireStation();
        fireStation.setStation("noStation");
        fireStation.setAddress("noAddress");

        when(fireStationRepository.updateExistingFireStationNumber(any(FireStation.class))).thenReturn(false);

        mockMvc.perform(put("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(fireStation)))
                .andExpect(status().isNotFound());

    }

    @Test
    void deleteExistingFireStation() throws Exception {
        String fireStationJson = "{ \"address\":\"44 groovy st\"}";

        when(fireStationRepository.delete(any(String.class))).thenReturn(true);
        when(fireStationRepository.findFireStation(any(String.class))).thenReturn(any(FireStation.class));

        mockMvc.perform(delete("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(fireStationJson))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteExistingFireStation_fail_to_delete() throws Exception {
        String fireStationJson = "{ \"address\":\"random st\"}";


        when(fireStationRepository.delete(any(String.class))).thenReturn(false);
        when(fireStationRepository.findFireStation(any(String.class))).thenReturn(null);

        mockMvc.perform(delete("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(fireStationJson))
                .andExpect(status().isNotFound());
    }
}