package com.safetynet.alerts.service;

import com.safetynet.alerts.view.FireResponse;
import com.safetynet.alerts.view.Resident;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * This service retrieves phone numbers for residents within the coverage area of a specific fire station.
 * It relies on the {@link FireResponseService} to obtain fire response information.
 * The service then extracts phone numbers from residents associated with that fire station.
 */
@Slf4j
@Service
public class PhoneAlertResponseService {
    @Autowired
    private FireResponseService fireResponseService;

    public List<String> findPhoneAlert(String fireStationNumber) {
        Map<String, FireResponse> fireResponseMap = fireResponseService.getFireResponse();
        Set<String> phoneNumbers = new HashSet<>();

        for (FireResponse fireResponse : fireResponseMap.values()) {

            if (fireResponse.getFireStationNumber().equalsIgnoreCase(fireStationNumber)) {
                List<Resident> residentList = fireResponse.getResidents();

                for (Resident resident : residentList) {
                    phoneNumbers.add(resident.getPhone());
                }
            }
        }

        return new ArrayList<>(phoneNumbers);
    }
}
