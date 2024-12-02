package com.safetynet.alerts.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.model.DataObject;
import com.safetynet.alerts.model.FireStation;
import jakarta.annotation.PostConstruct;
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

    @PostConstruct
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

    private FireStation findByAddress(String address) {
        return fireStationList.stream().filter(existingFireStation -> existingFireStation.getAddress().trim().equalsIgnoreCase(address.trim())).findFirst().orElse(null);
    }

    public FireStation findFireStation(String address) {
        return findByAddress(address);
    }

    public boolean updateExistingFireStationNumber(FireStation fireStation) {
        boolean updated = true;

        FireStation existingFireStation = findByAddress(fireStation.getAddress());
        /*
         *Retrieving an object from fireStationList via findByAddress returns a reference,
         *not a copy. Modifying the object through this reference updates the original in the list,
         *as both share the same memory location.
         */
        if (existingFireStation != null) {
            existingFireStation.setStation(fireStation.getStation());
        } else {
            updated = false;
        }
        return updated;
    }

    public boolean delete(String address) {
        boolean deleted = false;
        FireStation fireStationToDelete = findByAddress(address);

        if (fireStationToDelete != null) {
            fireStationList.remove(fireStationToDelete);
            deleted = true;
        } else {
            log.error("Unable to find the fireStation from the address: {}", address);
        }
        return deleted;
    }

    public boolean save(FireStation fireStation) {
        boolean result = false;

        if (!fireStationList.contains(fireStation)) {
            result = fireStationList.add(fireStation);
        } else {
            log.info("Fire station is reserved for existing department ");
        }
        return result;
    }
}
