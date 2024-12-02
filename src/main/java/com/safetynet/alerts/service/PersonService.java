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
    private Map<String, List<MedicalRecord>> medicalRecordMap = new HashMap<>();
    private final Map<String, PersonInfoView> personInfoViewMap = new HashMap<>();

    /**
     * Finds person information based on the given first and last names.
     *
     * @param firstName the first name of the person to find
     * @param lastName  the last name of the person to find
     * @return a list of person information views, each containing the person's name, address, email, age, medications, and allergies
     */
    public List<PersonInfoView> findPersonInfo(@NotBlank String firstName, @NotBlank String lastName) {
        init();
        List<PersonInfoView> personInfoViewList = new ArrayList<>();

        for (PersonInfoView personInfoView : personInfoViewMap.values()) {
            String[] splitName = personInfoView.getName().split(" ");
            String personFirstName = splitName[0];
            String personLastName = splitName[1];

            if (personFirstName.equalsIgnoreCase(firstName) && personLastName.equalsIgnoreCase(lastName)) {
                personInfoViewList.add(personInfoView);
            }
        }

        return personInfoViewList;
    }

    @PostConstruct
    private void init() {
        populatePersonAndMedicalRecordMaps();
        populatePersonInfoServiceMaps();
    }

    /**
     * Find brith date from format MM/DD/YYYY
     *
     * @param birthDate
     * @return int value if age. ex) 25.
     */
    private int findAge(String birthDate) {
        int currentYear = LocalDate.now().getYear();

        String[] birthDateSplit = birthDate.split("/"); // ex) String [03, 25, 1988]
        int birthYear = Integer.parseInt(birthDateSplit[2]); // 1988
        int age = currentYear - birthYear;

        return age; // 2024 - 1988
    }

    private void populatePersonAndMedicalRecordMaps() {
        medicalRecordMap = medicalRecordRepository.findAll().stream().collect(Collectors.groupingBy(medicalRecord -> medicalRecord.getUniqueIdentifier()));

        personMap = personRepository.findAll().stream().collect(Collectors.toMap(person -> person.getFirstName() + "_" + person.getLastName() + "_" + person.getEmail(), // Unique key per person including address
                person -> person));
    }

    private void populatePersonInfoServiceMaps() {
        personInfoViewMap.clear(); // Clear the map to avoid duplicates on multiple calls

        personMap.forEach((personKey, person) -> {
            List<MedicalRecord> medicalRecords = medicalRecordMap.get(person.getUniqueIdentifier());

            if (medicalRecords != null && !medicalRecords.isEmpty()) {
                int index = 0;
                String peronInfoKey = person.getUniqueIdentifier() + "_" + medicalRecords.get(index).getBirthdate();
                if (personInfoViewMap.containsKey(peronInfoKey)) {
                    index++;
                }

                MedicalRecord medicalRecord = medicalRecords.get(index); // Assuming one-to-one relationship

                String key = person.getUniqueIdentifier() + "_" + medicalRecord.getBirthdate();

                // Construct PersonInfoView
                String name = person.getFirstName() + " " + person.getLastName();
                String address = person.getAddress() + ", " + person.getCity() + ", " + person.getZip();
                String email = person.getEmail();
                int age = findAge(medicalRecord.getBirthdate());
                List<String> medications = medicalRecord.getMedications();
                List<String> allergies = medicalRecord.getAllergies();

                // Put the new entry in the map
                personInfoViewMap.put(key, new PersonInfoView(name, address, email, age, medications, allergies));
            }
        });
    }
}
