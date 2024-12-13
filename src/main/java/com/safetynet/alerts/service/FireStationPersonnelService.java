package com.safetynet.alerts.service;

import com.safetynet.alerts.view.FireResponse;
import com.safetynet.alerts.view.FireStationPersonnel;
import com.safetynet.alerts.view.OtherPersonInfo;
import com.safetynet.alerts.view.Resident;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class FireStationPersonnelService {
    @Autowired
    private FireResponseService fireResponseService;

    public FireStationPersonnel findFireStationPersonnel(String stationNumber) {
        int adultCount;
        int childCount;
        Map<String, FireResponse> fireResponseMap = fireResponseService.getFireResponse();
        FireStationPersonnel fireStationPersonnel = new FireStationPersonnel();
        List<OtherPersonInfo> otherPersonInfoList = new ArrayList<>();


        for (FireResponse fireResponse : fireResponseMap.values()) {
            List<Resident> residentList = fireResponse.getResidents();

            if (fireResponse.getFireStationNumber().equalsIgnoreCase(stationNumber)) {

                if (fireStationPersonnel.getStationNumber() == null) {
                    fireStationPersonnel.setStationNumber(stationNumber);
                }

                if (fireStationPersonnel.getOtherPersonInfoList() == null) {
                    fireStationPersonnel.setOtherPersonInfoList(new ArrayList<>());
                }

                for (Resident resident : residentList) {
                    String[] splitName = resident.getName().split(" ");

                    OtherPersonInfo otherPersonInfo = OtherPersonInfo.builder()
                            .firstName(splitName[0])
                            .lastName(splitName[1])
                            .age(resident.getAge())
                            .address(fireResponse.getAddress())
                            .build();

                    otherPersonInfoList.add(otherPersonInfo);
                }
            }
        }
        return null;
    }

    //TODO:: working to create a helper method.
//    private OtherPersonInfo
}
