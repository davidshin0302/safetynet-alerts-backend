package com.safetynet.alerts.service;

import com.safetynet.alerts.view.FireResponse;
import com.safetynet.alerts.view.Resident;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This service retrieves phone numbers for residents within the coverage area of a specific fire station.
 * It relies on the `FireResponseService` to obtain fire response information for a given fire station number.
 * The service then extracts phone numbers from residents associated with that fire station.
 */
@Slf4j
@Service
public class PhoneAlertResponseService {
    @Autowired
    private FireResponseService fireResponseService;

    //TODO::[BUG]  Need to fix fail to add all the resident when matching station number is found. *Duplicate line number. Check each household!
    public List<String> findPhoneAlert(String fireStationNumber) {
        Map<String, FireResponse> fireResponseMap = fireResponseService.getFireResponse();
        List<String> phoneNumbers = new ArrayList<>();

        for (FireResponse fireResponse : fireResponseMap.values()) {

            if (fireResponse.getFireStationNumber().equalsIgnoreCase(fireStationNumber)) {
                List<Resident> residentList = fireResponse.getResidents();
                phoneNumbers = findPhoneNumbers(residentList);
                break;
            }
        }

        return phoneNumbers;
    }

    private List<String> findPhoneNumbers(List<Resident> residentList) {
        return residentList.stream().map(Resident::getPhone).toList();
    }
}
