package com.safetynet.alerts.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.model.DataObject;
import com.safetynet.alerts.model.FireStation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class FireStationRepositoryTest {
    @MockBean
    private FireStationRepository fireStationRepository;

    private ObjectMapper objectMapper = new ObjectMapper();

    private static final String TEST_FILE_PATH = "src/test/resources";

    @BeforeEach
    void setUp() throws IOException {
        when(fireStationRepository.findAll()).thenReturn(objectMapper.readValue(new File(TEST_FILE_PATH + "/testData.json"), DataObject.class).getFireStations());
    }

    @Test
    void testFindAll() {
        var fireStationList = fireStationRepository.findAll();

        assertNotNull(fireStationList);
        assertEquals(13, fireStationList.size());

        var fireStation = fireStationList.get(0);
        assertEquals("1509 Culver St", fireStation.getAddress());
        assertEquals("3", fireStation.getStation());
    }

    @Test
    void testFindALlEmpty() {
        when(fireStationRepository.findAll()).thenReturn(List.of());

        assertTrue(fireStationRepository.findAll().isEmpty());
        assertEquals(0, fireStationRepository.findAll().size());
    }

    @Test
    void testFindByStation() {
        var fireStation = new FireStation();
        fireStation.setAddress("123 main st");
        fireStation.setStation("99");

        when(fireStationRepository.findByStation(fireStation)).thenReturn(fireStation);

        FireStation foundFireStation = fireStationRepository.findByStation(fireStation);
        assertNotNull(fireStation);
        assertEquals(fireStation, foundFireStation);
        assertEquals(fireStation.getAddress(), foundFireStation.getAddress());
        assertEquals(fireStation.getStation(), foundFireStation.getStation());

    }

    @Test
    void testNonExistingAddress() {
        var fireStation = fireStationRepository.findByStation(new FireStation());

        when(fireStationRepository.findByStation(fireStation)).thenReturn(null);

        assertNull(fireStation);
    }

    @Test
    void testUpdateExistingFireStationAddress() {
        when(fireStationRepository.updateExistingFireStationAddress(any(FireStation.class))).thenReturn(true);

        var fireStation = fireStationRepository.findAll().get(0);
        fireStation.setAddress("123 main st");

        assertTrue(fireStationRepository.updateExistingFireStationAddress(fireStation));
        assertEquals(fireStation.getAddress(), fireStationRepository.findAll().get(0).getAddress());
    }

    @Test
    void testNonExistingAddress_updateExistingFireStation() throws JsonProcessingException {
        var nonExistingJson = "{ \"address\":\"\", \"station\":\"\"}";
        var nonExpectedFireStation = objectMapper.readValue(nonExistingJson, FireStation.class);

        when(fireStationRepository.updateExistingFireStationAddress(nonExpectedFireStation)).thenReturn(false);

        assertFalse(fireStationRepository.updateExistingFireStationAddress(nonExpectedFireStation));
    }

    @Test
    void testDelete() {
        var fireStationList = new ArrayList<>(fireStationRepository.findAll());
        var fireStationToDelete = fireStationList.get(0);

        when(fireStationRepository.delete(fireStationToDelete)).thenReturn(true);

        fireStationList.remove(fireStationToDelete);

        when(fireStationRepository.findAll()).thenReturn(fireStationList);

        assertTrue(fireStationRepository.delete(fireStationToDelete));
        assertEquals(fireStationList.size(), fireStationRepository.findAll().size());
    }

    @Test
    void testSave() {
        var fireStationList = new ArrayList<>(fireStationRepository.findAll());


        var fireStation = new FireStation();
        fireStation.setAddress("123 main st");
        fireStation.setStation("99");

        when(fireStationRepository.save(fireStation)).thenReturn(true);
        fireStationList.add(fireStation);

        when(fireStationRepository.findAll()).thenReturn(fireStationList);
        assertTrue(fireStationRepository.save(fireStation));
        assertTrue(fireStationRepository.findAll().contains(fireStation));
        assertEquals(fireStationList.size(), fireStationRepository.findAll().size());
    }
}