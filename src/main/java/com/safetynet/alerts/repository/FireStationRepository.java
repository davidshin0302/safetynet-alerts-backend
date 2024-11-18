package com.safetynet.alerts.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.model.DataObject;
import com.safetynet.alerts.model.FireStation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Repository
@Slf4j
public class FireStationRepository {

    private final List<FireStation> fireStationList = new ArrayList<>();

    public FireStationRepository() {
        loadFireStationData();
    }

    private void loadFireStationData() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String filePath = "src/main/resources/data.json";
            DataObject dataObject = objectMapper.readValue(new File(filePath), DataObject.class);
            fireStationList.addAll(dataObject.getFireStations());
        } catch (IOException | RuntimeException ex) {
            throw new RuntimeException(ex);
        }
    }

    public List<FireStation> findAll() {
        return fireStationList;
    }

    public FireStation findByStation(String station) {
        return findAll().stream()
                .filter(existingFireStation -> existingFireStation.getStation().equals(station))
                .findFirst()
                .orElse(null);
    }

    public boolean updateExistingFireStationAddress(FireStation fireStation) {
        boolean updated = true;

        FireStation existingFireStation = findByStation(fireStation.getStation());

        if (existingFireStation != null) {
            existingFireStation.setAddress(fireStation.getAddress());
            save(existingFireStation);
        } else {
            updated = false;
        }
        return updated;
    }

    public boolean delete(String station) {
        boolean deleted = false;
        FireStation fireStationToDelete = findByStation(station);

        if (fireStationToDelete != null) {
            findAll().remove(fireStationToDelete);
            deleted = true;
        } else {
            log.error("Unable to find the fireStation");
        }
        return deleted;
    }

    public boolean save(FireStation fireStation) {
        boolean result = false;

        if (findByStation(fireStation.getStation()) == null) {
            result = findAll().add(fireStation);
        } else {
            log.info("Fire station is reserved for existing department ");
        }
        return result;
    }
}
