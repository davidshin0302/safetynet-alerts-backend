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

/**
 * This service retrieves community email addresses for a given city.
 * It utilizes the `PersonRepository` to access person data and then groups individuals
 * based on their city of residence. Emails are extracted from the grouped data
 * to form a list of community email addresses for the requested city.
 */
@Slf4j
@Service
public class CommunityEmailService {
    private Map<String, List<Person>> communityEmailsMap = new HashMap<>();
    @Autowired
    private PersonRepository personRepository;

    public List<String> findCommunityEmailsByCity(String city) {
        loadCommunityEmails();
        List<String> communityEmailList = List.of();

        if (communityEmailsMap.get(city) != null) {
            communityEmailList = communityEmailsMap.get(city).stream().map(Person::getEmail).toList();
        }
        return communityEmailList;
    }

    private void loadCommunityEmails() {
        List<Person> personList = personRepository.findAll();

        communityEmailsMap = personList.stream().collect(Collectors.groupingBy(Person::getCity)); // same as person -> person.getCity();
    }
}
