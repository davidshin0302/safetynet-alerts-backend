package com.safetynet.alerts.service;

import com.safetynet.alerts.model.FireStation;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.FireStationRepository;
import com.safetynet.alerts.repository.PersonRepository;
import com.safetynet.alerts.view.FireResponse;
import com.safetynet.alerts.view.MedicalInfo;
import com.safetynet.alerts.view.PersonInfoView;
import com.safetynet.alerts.view.Resident;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class FireResponseService {
    private final Map<String, FireResponse> fireResponseMap = new HashMap<>();
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private FireStationRepository fireStationRepository;
    @Autowired
    private PersonService personService;
    @Autowired
    private Resident resident;
    @Autowired
    private MedicalInfo medicalInfo;

    public Map<String, FireResponse> findFireResponse(String address) {
        loadFireResponseService();

        return fireResponseMap;
    }

    private void loadFireResponseService() {
        List<Person> personList = personRepository.findAll();

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

                String stationNumber = findStationNumber(tempMap.get(person.getAddress()).getFireStationNumber());

                setResident(person.getFirstName(), person.getLastName(), person.getAddress(), person.getPhone());
                residents.add(resident);
                fireResponse.setFireStationNumber(stationNumber);
            }
        }

        fireResponseMap.putAll(tempMap);
    }

    private String findStationNumber(String stationNumber) {
        String result = stationNumber;

        if (stationNumber == null) {
            List<FireStation> fireStationList = fireStationRepository.findAll();

            FireStation matchingFireStation = fireStationList.stream()
                    .filter(fireStation -> fireStation.getAddress().equalsIgnoreCase(person.getAddress()))
                    .findFirst()
                    .orElse(null);

            if (matchingFireStation != null) {
                result = matchingFireStation.getStation();
            }
        }

        return result;
    }

    private void setResident(String firstName, String lastName, String address, String phoneNumber) {
        List<PersonInfoView> personInfoViewList = personService.findPersonInfo(firstName, lastName);
        PersonInfoView personInfoView = personInfoViewList.stream().filter(resident -> resident.getAddress().equalsIgnoreCase(address)).findFirst().orElse(null);

        if (personInfoView != null) {
            medicalInfo.setMedicatinos(personInfoView.getMedications());
            medicalInfo.setAllergies(personInfoView.getAllergies());

            resident.setName(personInfoView.getName());
            resident.setPhone(phoneNumber);
            resident.setAge(personInfoView.getAge());
            resident.setMedicalInfo(medicalInfo);
        }
    }
}
