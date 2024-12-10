package com.safetynet.alerts.service;

import com.safetynet.alerts.view.FireResponse;
import com.safetynet.alerts.view.Resident;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class PhoneAlertResponseService {
    @Autowired
    private FireResponseService fireResponseService;

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
