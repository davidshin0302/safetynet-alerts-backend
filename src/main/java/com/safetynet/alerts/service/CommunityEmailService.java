package com.safetynet.alerts.service;

import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.PersonRepository;
import com.safetynet.alerts.view.CommunityEmail;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class CommunityEmailService {
    private final Map<String, List<CommunityEmail>> communityEmailsMap = new HashMap<>();
    @Autowired
    private PersonRepository personRepository;

    public Map<String, List<CommunityEmail>> findCommunityEmailsByCity(@NotBlank String city) {
        List<Person> personList = personRepository.findAll();

        for (Person person : personList) {
            if (city.equalsIgnoreCase(person.getCity())) {
                if (communityEmailsMap.containsKey(city)) {
                    List<CommunityEmail> communityEmailServiceList = communityEmailsMap.get(city);
                    CommunityEmail communityEmail = new CommunityEmail();
                    communityEmail.setEmail(person.getEmail());

                    communityEmailServiceList.add(communityEmail);
                } else {
                    List<CommunityEmail> communityEmailList = new ArrayList<>();
                    CommunityEmail communityEmail = new CommunityEmail();
                    communityEmail.setEmail(person.getEmail());

                    communityEmailList.add(communityEmail);

                    communityEmailsMap.put(city, communityEmailList);
                }
            }
        }
        return communityEmailsMap;
    }
}
