package com.safetynet.alerts.service;

import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.MedicalRecordRepository;
import com.safetynet.alerts.repository.PersonRepository;
import com.safetynet.alerts.view.PersonInfoView;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Retrieve person details (/personInfo?firstName=<firstName>&lastName=<lastName>).
 * Retrieve community emails (/communityEmail?city=<city>).
 */
@Slf4j
@Service
@AllArgsConstructor
@NoArgsConstructor
public class PersonService {
    @Autowired
    private MedicalRecordRepository medicalRecordRepository;

    @Autowired
    private PersonRepository personRepository;

    /**
     * Finds person information based on the given first and last names.
     *
     * @param firstName the first name of the person to find
     * @param lastName  the last name of the person to find
     * @return a list of person information views, each containing the person's name, address, email, age, medications, and allergies
     */
    public List<PersonInfoView> findPersonInfo(@NotBlank String firstName, @NotBlank String lastName) {
        List<Person> personList = personRepository.findAll();
        List<PersonInfoView> personInfoViewList = new ArrayList<>();

        for (Person person : personList) {
            if (person.getFirstName().equalsIgnoreCase(firstName) && person.getLastName().equalsIgnoreCase(lastName)) {
                MedicalRecord medicalRecord = medicalRecordRepository.findRecord(person.getFirstName(), person.getLastName());
                if (medicalRecord != null) {
                    String name = person.getFirstName() + " " + person.getLastName();
                    String address = person.getAddress() + " " + person.getCity() + ", " + person.getZip();
                    String email = person.getEmail();
                    int age = findAge(medicalRecord);

                    PersonInfoView personInfoView = new PersonInfoView(name, address, email, age, medicalRecord.getMedications(), medicalRecord.getAllergies());
                    personInfoViewList.add(personInfoView);
                } else {
                    log.info("Medical record is not found from the Person info: {}", person);
                }
            }
            log.info("[PersonService] Unable to find the person with name, {}", firstName + " " + lastName);
        }
        return personInfoViewList;
    }

    /**
     * Find brith date from format MM/DD/YYYY
     *
     * @param medicalRecord
     * @return int value if age. ex) 25.
     */
    private int findAge(MedicalRecord medicalRecord) {
        String birthDate = medicalRecord.getBirthdate();

        int year = LocalDate.now().getYear();
        String[] birthDateSplit = birthDate.split("/"); // ex) String [03, 25, 1988]
        int getPersonBirthYear = Integer.parseInt(birthDateSplit[0]); // 1988

        return Integer.min(year, getPersonBirthYear); // 2024 - 1988
    }
}
