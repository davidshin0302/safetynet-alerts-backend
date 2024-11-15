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

    public List<FireStation> findAll() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String filePath = "src/main/resources/data.json";
            DataObject dataObject = objectMapper.readValue(new File(filePath), DataObject.class);
            return dataObject.getFireStations();
        } catch (IOException | RuntimeException ex) {
            log.error(ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    public FireStation findByStation(FireStation fireStation) {
        return findAll().stream()
                .filter(existingFireStation -> existingFireStation.equals(fireStation))
                .findFirst()
                .orElse(null);
    }

    public boolean updateExistingFireStationAddress(FireStation fireStation) {
        boolean updated = true;

        FireStation existingFireStation = findByStation(fireStation);

        if (existingFireStation != null) {
            existingFireStation.setAddress(fireStation.getAddress());
            save(existingFireStation);
        } else {
            updated = false;
        }
        return updated;
    }

    public boolean delete(FireStation fireStation) {
        boolean deleted = false;

        if (fireStation != null) {
            findAll().remove(fireStation);
            deleted = true;
        } else {
            log.error("Unable to find the fireStation");
        }
        return deleted;
    }

    public boolean save(FireStation fireStation) {
        boolean result = false;

        if (findByStation(fireStation) == null) {
            result = findAll().add(fireStation);
        } else {
            log.info("Fire station is reserved for existing department ");
        }
        return result;
    }
}
