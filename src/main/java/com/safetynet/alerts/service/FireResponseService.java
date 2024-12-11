package com.safetynet.alerts.service;

import com.safetynet.alerts.model.FireStation;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.FireStationRepository;
import com.safetynet.alerts.repository.PersonRepository;
import com.safetynet.alerts.view.FireResponse;
import com.safetynet.alerts.view.MedicalInfo;
import com.safetynet.alerts.view.PersonInfo;
import com.safetynet.alerts.view.Resident;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This service is responsible for generating fire response information based on addresses.
 * It retrieves data from various sources, including `PersonRepository`, `FireStationRepository`, and `PersonInfoService`.
 * The service builds a map where keys are addresses and values are `FireResponse` objects.
 * Each `FireResponse` object contains details about residents at an address (name, phone, age, medical information),
 * as well as the assigned fire station number for that address.
 * <p>
 * The service offers two methods:
 * - `findFireResponse(String address)`: Retrieves the `FireResponse` object for a specific address.
 * - `getFireResponse()`: Returns the entire map of address-to-fire response information.
 */
@Slf4j
@Service
public class FireResponseService {
    private final Map<String, FireResponse> fireResponseMap = new HashMap<>();
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private FireStationRepository fireStationRepository;
    @Autowired
    private PersonInfoService personInfoService;
    private List<FireStation> fireStationList;


    public FireResponse findFireResponse(String address) {
        loadFireResponseService();

        return fireResponseMap.getOrDefault(address, new FireResponse());
    }

    public Map<String, FireResponse> getFireResponse() {
        loadFireResponseService();
        return fireResponseMap;
    }

    private void loadFireResponseService() {
        List<Person> personList = personRepository.findAll();
        fireStationList = fireStationRepository.findAll();

        Map<String, FireResponse> tempMap = new HashMap<>();

        for (FireStation fireStation : fireStationList) {
            tempMap.put(fireStation.getAddress(), new FireResponse());
        }

        for (Person person : personList) {

            if (tempMap.containsKey(person.getAddress())) {
                FireResponse fireResponse = tempMap.get(person.getAddress());
                List<Resident> residents = fireResponse.getResidents();

                if (residents == null) {
                    residents = new ArrayList<>();
                    fireResponse.setResidents(residents);
                }

                String matchingStationNumber = findStationNumber(person.getAddress());

                Resident resident = setResident(person.getFirstName(), person.getLastName(), person.getAddress(), person.getPhone());
                residents.add(resident);
                fireResponse.setFireStationNumber(matchingStationNumber);
                fireResponse.setAddress(person.getAddress());
            }
        }

        fireResponseMap.putAll(tempMap);
    }

    private String findStationNumber(String address) {
        FireStation matchingFireStation = fireStationList.stream()
                .filter(fireStation -> fireStation.getAddress().equalsIgnoreCase(address))
                .findFirst()
                .orElse(null);

        return matchingFireStation.getStation();
    }

    private Resident setResident(String firstName, String lastName, String address, String phoneNumber) {
        MedicalInfo medicalInfo = new MedicalInfo();
        Resident resident = new Resident();

        List<PersonInfo> personInfoList = personInfoService.findPersonInfo(firstName, lastName);
        PersonInfo matchinPersonInfo = personInfoList.stream().filter(personInfoView -> {
            String[] splitAddress = personInfoView.getAddress().split(",");

            return address.equalsIgnoreCase(splitAddress[0]);
        }).findFirst().orElse(null);

        if (matchinPersonInfo != null) {
            medicalInfo.setMedications(matchinPersonInfo.getMedications());
            medicalInfo.setAllergies(matchinPersonInfo.getAllergies());

            resident.setName(matchinPersonInfo.getName());
            resident.setPhone(phoneNumber);
            resident.setAge(matchinPersonInfo.getAge());
            resident.setMedicalInfo(medicalInfo);
        }

        return resident;
    }
}
