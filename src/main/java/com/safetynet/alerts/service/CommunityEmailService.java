package com.safetynet.alerts.service;

import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.PersonRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CommunityEmailService {
    private Map<String, List<Person>> communityEmailsMap = new HashMap<>();
    @Autowired
    private PersonRepository personRepository;

    public List<String> findCommunityEmailsByCity(String city) {
        loadCommunityEmails();
        List<String> personList;

        return communityEmailsMap.get(city).stream().map(Person::getEmail).collect(Collectors.toList());
    }

    private void loadCommunityEmails() {
        List<Person> personList = personRepository.findAll();

        communityEmailsMap = personList.stream().collect(Collectors.groupingBy(Person::getCity)); // same as person -> person.getCity();
    }
}
