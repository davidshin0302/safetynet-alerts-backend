package com.safetynet.alerts.service;

import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.MedicalRecordRepository;
import com.safetynet.alerts.repository.PersonRepository;
import com.safetynet.alerts.view.PersonInfoView;
import jakarta.annotation.PostConstruct;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Retrieve person details (/personInfo?firstName=<firstName>&lastName=<lastName>).
 * Retrieve community emails (/communityEmail?city=<city>).
 */
@Slf4j
@Service
public class PersonService {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private MedicalRecordRepository medicalRecordRepository;

    private Map<String, Person> personMap = new HashMap<>();
    private Map<String, MedicalRecord> medicalRecordMap = new HashMap<>();
    private Map<String, PersonInfoView> personInfoViewMap = new HashMap<>();

    /**
     * Finds person information based on the given first and last names.
     *
     * @param firstName the first name of the person to find
     * @param lastName  the last name of the person to find
     * @return a list of person information views, each containing the person's name, address, email, age, medications, and allergies
     */
    public List<PersonInfoView> findPersonInfo(@NotBlank String firstName, @NotBlank String lastName) {
        return new ArrayList<>(personInfoViewMap.values());
    }

    @PostConstruct
    private void init(){
        populatePersonAndMedicalRecordMaps();;
        populatePersonInfoServiceMaps();
    }

    /**
     * Find brith date from format MM/DD/YYYY
     *
     * @param medicalRecord
     * @return int value if age. ex) 25.
     */
    private int findAge(MedicalRecord medicalRecord) {
        String birthDate = medicalRecord.getBirthdate();

        int currentYear = LocalDate.now().getYear();

        String[] birthDateSplit = birthDate.split("/"); // ex) String [03, 25, 1988]
        int birthYear = Integer.parseInt(birthDateSplit[2]); // 1988
        int age = currentYear - birthYear;

        return age; // 2024 - 1988
    }

    private void populatePersonAndMedicalRecordMaps() {
        medicalRecordMap = medicalRecordRepository.findAll()
                .stream()
                .collect(Collectors.toMap(
                        medicalRecord -> medicalRecord.getUniqueIdentifier(), //same as -> MedicalRecord::getUniqueIdentifier
                        medicalRecord -> medicalRecord
                ));

        personMap = personRepository.findAll()
                .stream()
                .filter(person -> medicalRecordMap.containsKey(person.getPartialIdentifier()))
                .collect(Collectors.toMap(
                        person -> person.getPartialIdentifier(),
                        person -> person
                ));
    }

    private void populatePersonInfoServiceMaps() {
        if (personInfoViewMap.isEmpty()) {
            personInfoViewMap = personMap.entrySet().stream()
                    .collect(Collectors.toMap(
                            personEntry -> {
                                Person person = personEntry.getValue();
                                MedicalRecord medicalRecord = medicalRecordMap.get(personEntry.getKey());
                                return person.getUniqueIdentifier(medicalRecord.getBirthdate());
                            },
                            personEntry -> {
                                Person person = personEntry.getValue();
                                MedicalRecord medicalRecord = medicalRecordMap.get(personEntry.getKey());
                                String name = person.getFirstName() + " " + person.getLastName();
                                String address = person.getAddress() + ", " + person.getCity() + ", " + person.getZip();
                                String email = person.getEmail();
                                int age = findAge(medicalRecord);
                                List<String> medications = medicalRecord.getMedications();
                                List<String> allergies = medicalRecord.getAllergies();

                                return new PersonInfoView(name, address, email, age, medications, allergies);
                            }
                    ));
        }
    }
}
