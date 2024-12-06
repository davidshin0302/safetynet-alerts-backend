package com.safetynet.alerts.service;

import com.safetynet.alerts.view.ChildInfo;
import com.safetynet.alerts.view.PersonInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ChildAlertResponseService {
    @Autowired
    private PersonInfoService personInfoService;
    private List<ChildInfo> childInfoList;


    public List<ChildInfo> findChildAlert(String address) {
    }

    private void loadChildAlertData(){
        Map<String, PersonInfo> personInfoViewMap = personInfoService.getPersonInfoViewMap();

        for(PersonInfo personInfo : personInfoViewMap.values()){
            if(personInfo.getAge() < 18 ) {
                String[] splitName = personInfo.getName().split(" ");
                String firstName = splitName[0];
                String lastName = splitName[1];
                ChildInfo childInfo = new ChildInfo();

                childInfo.setFirstName(firstName);
                childInfo.setLastName(lastName);
                childInfo.setAge(personInfo.getAge());
            }
        }
    }
}
