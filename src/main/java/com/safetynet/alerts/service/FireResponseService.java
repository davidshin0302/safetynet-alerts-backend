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
    private List<FireStation> fireStationList;


    public FireResponse findFireResponse(String address) {
        loadFireResponseService();

        return fireResponseMap.getOrDefault(address, new FireResponse());
    }

    public Map<String, FireResponse> getFireResponse(){
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

        List<PersonInfoView> personInfoViewList = personService.findPersonInfo(firstName, lastName);
        PersonInfoView matchinPersonInfoView = personInfoViewList.stream().filter(personInfoView -> {
            String[] splitAddress = personInfoView.getAddress().split(",");

            return address.equalsIgnoreCase(splitAddress[0]);
        }).findFirst().orElse(null);

        if (matchinPersonInfoView != null) {
            medicalInfo.setMedicatinos(matchinPersonInfoView.getMedications());
            medicalInfo.setAllergies(matchinPersonInfoView.getAllergies());

            resident.setName(matchinPersonInfoView.getName());
            resident.setPhone(phoneNumber);
            resident.setAge(matchinPersonInfoView.getAge());
            resident.setMedicalInfo(medicalInfo);
        }

        return resident;
    }
}
