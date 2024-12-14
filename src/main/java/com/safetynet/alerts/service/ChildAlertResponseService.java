package com.safetynet.alerts.service;

import com.safetynet.alerts.view.ChildAlertResponse;
import com.safetynet.alerts.view.ChildInfo;
import com.safetynet.alerts.view.OtherPersonInfo;
import com.safetynet.alerts.view.PersonInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This service is responsible for retrieving and processing child alert information based on a given address.
 * It fetches person information from the `PersonInfoService`, filters the data based on age and address,
 * and constructs a `ChildAlertResponse` object containing child and other person details.
 */
@Slf4j
@Service
public class ChildAlertResponseService {
    private final List<ChildInfo> childInfoList = new ArrayList<>();
    private final List<OtherPersonInfo> otherPersonInfoList = new ArrayList<>();
    @Autowired
    private PersonInfoService personInfoService;

    public ChildAlertResponse findChildAlert(String address) {
        loadChildAlertData(address);

        return ChildAlertResponse.builder()
                .children(childInfoList)
                .otherPersons(otherPersonInfoList)
                .build();
    }

    private void loadChildAlertData(String address) {
        childInfoList.clear();
        otherPersonInfoList.clear();
        ;

        Map<String, PersonInfo> personInfoViewMap = personInfoService.getPersonInfoViewMap();

        for (PersonInfo personInfo : personInfoViewMap.values()) {
            String[] fullName = personInfo.getName().split(" ");
            String[] splitAddress = personInfo.getAddress().split(",");

            if (personInfo.getAge() < 18 && address.equalsIgnoreCase(splitAddress[0])) {
                ChildInfo childInfo = new ChildInfo();

                childInfo.setFirstName(fullName[0]);
                childInfo.setLastName(fullName[1]);
                childInfo.setAge(personInfo.getAge());

                childInfoList.add(childInfo);
            } else if (address.equalsIgnoreCase(splitAddress[0])) {
                OtherPersonInfo otherPersonInfo = new OtherPersonInfo();

                otherPersonInfo.setFirstName(fullName[0]);
                otherPersonInfo.setLastName(fullName[1]);
                otherPersonInfo.setAge(personInfo.getAge());
                otherPersonInfo.setAddress(splitAddress[0]);

                otherPersonInfoList.add(otherPersonInfo);
            }
        }
    }
}
