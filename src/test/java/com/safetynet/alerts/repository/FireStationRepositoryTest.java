package com.safetynet.alerts.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.model.DataObject;
import com.safetynet.alerts.model.FireStation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {FireStationRepository.class})
class FireStationRepositoryTest {
    @MockBean
    private FireStationRepository fireStationRepository;

    private List<FireStation> fireStationList;

    private static final String TEST_FILE_PATH = "src/test/resources";

    @BeforeEach
    void setUp() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        fireStationList = objectMapper.readValue(new File(TEST_FILE_PATH + "/testData.json"), DataObject.class).getFireStations();
    }

    @Test
    void testFindAll() {
        when(fireStationRepository.findAll()).thenReturn(fireStationList);

        List<FireStation> result = fireStationRepository.findAll();
        assertEquals(fireStationList.size(), result.size());
        assertEquals("1509 Culver St", fireStationList.get(0).getAddress());
        assertEquals("3", fireStationList.get(0).getStation());
    }

    @Test
    void testFindALlEmpty() {
        when(fireStationRepository.findAll()).thenReturn(List.of());
        List<FireStation> result = fireStationRepository.findAll();
        assertTrue(result.isEmpty());
    }

    @Test
    void testFindByStation() {
        FireStation fireStation = new FireStation();
        fireStation.setAddress("123 main st");
        fireStation.setStation("99");

        when(fireStationRepository.findByStation(fireStation)).thenReturn(fireStation);

        FireStation foundFireStation = fireStationRepository.findByStation(fireStation);
        assertNotNull(fireStation);
        assertEquals(fireStation, foundFireStation);

    }

    @Test
    void testFindByAddressNull() {
        when(fireStationRepository.findByStation(any(FireStation.class))).thenReturn(null);
        FireStation result = fireStationRepository.findByStation(any(FireStation.class));
        assertNull(result);
    }

    @Test
    void testUpdateExistingFireStationAddress() {
        when(fireStationRepository.updateExistingFireStationAddress(any(FireStation.class))).thenReturn(true);

        FireStation fireStation = fireStationList.get(0);
        fireStation.setAddress("123 main st");

        assertTrue(fireStationRepository.updateExistingFireStationAddress(fireStation));
        assertEquals(fireStation.getAddress(), fireStationList.get(0).getAddress());
    }

    @Test
    void testUpdateExistingFireStationAddressNotFound() {
        when(fireStationRepository.updateExistingFireStationAddress(any(FireStation.class))).thenReturn(false);

        FireStation fireStation = new FireStation();
        fireStation.setAddress("random st");
        fireStation.setStation("000");

        assertFalse(fireStationRepository.updateExistingFireStationAddress(fireStation));
    }

    @Test
    void testDelete() {
    }

    @Test
    void testSave() {
    }
}