package com.safetynet.alerts.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.model.DataObject;
import com.safetynet.alerts.model.FireStation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.File;
import java.io.IOException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class FireStationRepositoryTest {

    private FireStationRepository fireStationRepository;
    private ObjectMapper objectMapper = new ObjectMapper();
    private List<FireStation> fireStationList;

    private static final String TEST_FILE_PATH = "src/test/resources";

    @BeforeEach
    void setUp() throws IOException {
        fireStationRepository = new FireStationRepository();
        fireStationList = objectMapper.readValue(new File(TEST_FILE_PATH + "/testData.json"), DataObject.class).getFireStations();
    }

    @Test
    void findAll() {
        assertNotNull(fireStationList);
        assertEquals(13, fireStationList.size());

        var fireStation = fireStationList.get(0);
        assertEquals("1509 Culver St", fireStation.getAddress());
        assertEquals("3", fireStation.getStation());
    }

    @Test
    void testFindByAddress() throws IOException {
        var findFireStationJson = "{ \"address\":\"1509 Culver St\", \"station\":\"3\" }";
        var fireStation = objectMapper.readValue(findFireStationJson, FireStation.class);

        var expectedFireStation = fireStationRepository.findByAddress(fireStation.getAddress());
        assertNotNull(expectedFireStation);
        assertEquals(fireStation, expectedFireStation);
        assertEquals(fireStation.getAddress(), expectedFireStation.getAddress());
        assertEquals(fireStation.getStation(), expectedFireStation.getStation());
    }

    @Test
    void testUpdateExistingFireStationAddress() throws IOException {
        //Verify update fire station number
        var findFireStation = "{ \"address\":\"1509 Culver St\", \"station\":\"99\" }";
        var expectedFireStation1 = objectMapper.readValue(findFireStation, FireStation.class);
        assertTrue(fireStationRepository.updateExistingFireStationNumber(expectedFireStation1));

        var expectedFireStation2 = fireStationRepository.findByAddress(expectedFireStation1.getAddress());
        assertEquals("1509 Culver St", expectedFireStation2.getAddress());
        assertEquals("99", expectedFireStation2.getStation());

        //Attempt to update from invalid data.
        var nonExpectedJson = "{ \"address\":\"\", \"station\":\"\" }";
        var nonExpectedFireStation = objectMapper.readValue(nonExpectedJson, FireStation.class);

        assertFalse(fireStationRepository.updateExistingFireStationNumber(nonExpectedFireStation));
    }

    @Test
    void testUpdateExistingFireStationAddress_actual_data() throws IOException {
        var findFireStationJson = "{ \"address\":\"1509 Culver St\", \"station\":\"3\" }";
        var expectedFireStation = objectMapper.readValue(findFireStationJson, FireStation.class);

        expectedFireStation.setAddress("123 main st");

        assertEquals("123 main st", expectedFireStation.getAddress());
        assertEquals("3", expectedFireStation.getStation());
    }

    @Test
    void testDelete() throws IOException {
        var newFireStation = "{ \"address\":\"505 groove St\", \"station\":\"88\" }";
        var fireStation = objectMapper.readValue(newFireStation, FireStation.class);

        fireStationRepository.save(fireStation);

        assertTrue(fireStationRepository.delete(fireStation.getAddress()));
        assertEquals(fireStationList.size(), fireStationRepository.findAll().size());
    }

    @Test
    void testDelete_invalid_address() throws IOException {
        var invalidFireStation = "{ \"address\":\"no address\"}";
        var fireStation = objectMapper.readValue(invalidFireStation, FireStation.class);

        assertFalse(fireStationRepository.delete(fireStation.getAddress()));
    }

    @Test
    void testSave() throws IOException {
        // Create test data
        var newFireStation = "{ \"address\":\"505 groove St\", \"station\":\"88\" }";
        var fireStation = objectMapper.readValue(newFireStation, FireStation.class);

        assertTrue(fireStationRepository.save(fireStation));

        //Verify the person is added to the repository
        var savedStation = fireStationRepository.findByAddress(fireStation.getAddress());
        assertNotNull(fireStationRepository.findByAddress(fireStation.getAddress()));
        assertEquals("505 groove St", savedStation.getAddress());
        assertEquals("88", savedStation.getStation());

        //Attempt to save a duplicate person
        assertFalse(fireStationRepository.save(fireStation));
    }
}
