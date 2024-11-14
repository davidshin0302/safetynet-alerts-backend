package com.safetynet.alerts.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.model.DataObject;
import com.safetynet.alerts.model.FireStation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
        when(fireStationRepository.findAll()).thenReturn(fireStationList);
    }

    @Test
    void testFindAll() {
        assertEquals(13, fireStationList.size());
        assertEquals("1509 Culver St", fireStationList.get(0).getAddress());
        assertEquals("3", fireStationList.get(0).getStation());
    }

    @Test
    void testFindALlEmpty() {
        when(fireStationRepository.findAll()).thenReturn(new ArrayList<>());
        List<FireStation> result = fireStationRepository.findAll();

        assertTrue(result.isEmpty());
    }

    @Test
    void testFindAllNull() {
        when(fireStationRepository.findAll()).thenReturn(null);
        List<FireStation> result = fireStationRepository.findAll();

        assertNull(result);
    }

//    @Test
//    void testFindByAddress() {
//        FireStation fireStation = new FireStation();
//        fireStation.setAddress("123 main st");
//        fireStation.setStation("99");
//
//        FireStation foundFireStation = fireStationRepository.findByAddress(fireStation);
//        assertNotNull(fireStation);
//        assertEquals(fireStation, foundFireStation.getAddress());
//
//    }

    @Test
    void testFindByAddressNull() {
        when(fireStationRepository.findByAddress(any(FireStation.class))).thenReturn(null);

        FireStation result = fireStationRepository.findByAddress(any(FireStation.class));

        assertNull(result);
    }

    @Test
    void testUpdateExistingFireStation() {
    }

    @Test
    void testDelete() {
    }

    @Test
    void testSave() {
    }
}