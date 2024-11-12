package com.safetynet.alerts.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.model.DataObject;
import com.safetynet.alerts.model.FireStation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Repository
@Slf4j
public class FireStationRepository {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String filePath = "src/main/resources/data.json";
    private List<FireStation> fireStationList;

    public FireStationRepository() {
        try {
            DataObject dataObject = objectMapper.readValue(new File(filePath), DataObject.class);
            fireStationList = dataObject.getFireStations();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<FireStation> findAll() {
        return fireStationList;
    }

    public FireStation findByAddress(FireStation fireStation) {
        return fireStationList.stream()
                .filter(existingFireStation -> existingFireStation.equals(fireStation))
                .findFirst()
                .orElse(null);
    }

    public boolean updateExistingFireStation(FireStation fireStation) {
        boolean updated = true;

        FireStation existingFireStation = findByAddress(fireStation);

        if (existingFireStation != null) {
            existingFireStation.setAddress(fireStation.getAddress());
            existingFireStation.setStation(fireStation.getStation());
            save(existingFireStation);
        } else {
            updated = false;
        }
        return updated;
    }

    public boolean delete(FireStation fireStation) {
        boolean deleted = false;

        if (fireStation != null) {
            fireStationList.remove(fireStation);
            deleted = true;
        } else {
            log.error("Unable to find the fireStation");
        }
        return deleted;
    }

    public boolean save(FireStation fireStation) {
        boolean saved = false;

        try {
            fireStationList.add(fireStation);
            objectMapper.writeValue(new File("src/main/resources/tempData.json"), fireStationList);
            saved = true;
        } catch (IOException ex) {
            log.error(ex.getMessage());
        }

        return saved;
    }
}
