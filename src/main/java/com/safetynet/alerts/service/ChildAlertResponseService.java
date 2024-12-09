package com.safetynet.alerts.service;

import com.safetynet.alerts.view.ChildAlertResponse;
import com.safetynet.alerts.view.ChildInfo;
import com.safetynet.alerts.view.OtherPersonInfo;
import com.safetynet.alerts.view.PersonInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Slf4j
@Service
public class ChildAlertResponseService {
    private final Set<ChildInfo> childInfoList = new HashSet<>();
    private final Set<OtherPersonInfo> otherPersonInfoList = new HashSet<>();
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
            String[] splitName = personInfo.getName().split(" ");
            String[] splitAddress = personInfo.getAddress().split(",");

            if (personInfo.getAge() < 18 && address.equalsIgnoreCase(splitAddress[0])) {
                ChildInfo childInfo = new ChildInfo();

                childInfo.setFirstName(splitName[0]);
                childInfo.setLastName(splitName[1]);
                childInfo.setAge(personInfo.getAge());

                childInfoList.add(childInfo);
            } else if (address.equalsIgnoreCase(splitAddress[0])) {
                OtherPersonInfo otherPersonInfo = new OtherPersonInfo();

                otherPersonInfo.setFirstName(splitName[0]);
                otherPersonInfo.setLastName(splitName[1]);
                otherPersonInfo.setAge(personInfo.getAge());
                otherPersonInfo.setAddress(splitAddress[0]);

                otherPersonInfoList.add(otherPersonInfo);
            }
        }
    }
}
