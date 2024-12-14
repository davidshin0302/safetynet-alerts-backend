package com.safetynet.alerts.service;

import com.safetynet.alerts.view.FireResponse;
import com.safetynet.alerts.view.FireStationPersonnel;
import com.safetynet.alerts.view.OtherPersonInfo;
import com.safetynet.alerts.view.Resident;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Service that retrieves personnel information (adult and child counts, and a list of personnel details) for a given fire station.
 * It uses data from the {@link FireResponseService} to gather resident information.
 */
@Slf4j
@Service
public class FireStationPersonnelService {
    @Autowired
    private FireResponseService fireResponseService;

    public FireStationPersonnel findFireStationPersonnel(String stationNumber) {
        int adultCount = 0;
        int childCount = 0;
        Map<String, FireResponse> fireResponseMap = fireResponseService.getFireResponse();
        List<OtherPersonInfo> otherPersonInfoList = new ArrayList<>();


        for (FireResponse fireResponse : fireResponseMap.values()) {
            List<Resident> residentList = fireResponse.getResidents();

            if (fireResponse.getFireStationNumber().equalsIgnoreCase(stationNumber)) {

                for (Resident resident : residentList) {
                    String[] fullName = resident.getName().split(" ");

                    //Build OtherPersonInfo that contain personnel name, age, address.
                    OtherPersonInfo otherPersonInfo = buildPersonInfo(fullName, resident, fireResponse.getAddress());
                    otherPersonInfoList.add(otherPersonInfo);

                    //Determine if personnel is adult or child.
                    if (resident.getAge() >= 18) {
                        adultCount++;
                    } else {
                        childCount++;
                    }
                }
            }
        }

        // Sort the list by age (descending - oldest to youngest)
        otherPersonInfoList.sort(Comparator.comparingInt(resident -> -resident.getAge()));

        return FireStationPersonnel.builder()
                .stationNumber(stationNumber)
                .adultCount(adultCount)
                .childCount(childCount)
                .otherPersonInfoList(otherPersonInfoList)
                .build();
    }

    private OtherPersonInfo buildPersonInfo(String[] fullName, Resident resident, String matchAddress) {
        return OtherPersonInfo.builder()
                .firstName(fullName[0])
                .lastName(fullName[1])
                .age(resident.getAge())
                .address(matchAddress)
                .build();
    }
}
