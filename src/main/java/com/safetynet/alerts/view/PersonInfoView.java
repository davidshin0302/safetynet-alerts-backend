package com.safetynet.alerts.view;

import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.MedicalRecordRepository;
import com.safetynet.alerts.repository.PersonRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;

//Might need to
@Getter
@Setter
public class PersonInfoView {
    @Autowired // should not Autowire in business model especially from view.
    PersonRepository personRepository;
    @Autowired
    MedicalRecordRepository medicalRecordRepository;

    private String name;
    private String address;
    private String email;
    private int age;
    private List<String> medications;
    private List<String> allergies;

    //Service class
    public PersonInfoView(Person person, MedicalRecord medicalRecord) {
        this.name = person.getFirstName() + " " + person.getLastName();
        this.address = person.getAddress();
        this.email = person.getEmail();
        this.age = findAge(person);
        this.medications = medicalRecordRepository.findRecord(person.getFirstName(), person.getLastName()).getMedications();
        this.allergies = medicalRecordRepository.findRecord(person.getFirstName(), person.getLastName()).getAllergies();
    }

    private int findAge(Person person) {
        MedicalRecord medicalRecord = medicalRecordRepository.findRecord(person.getFirstName(), person.getLastName());
        String birthDate = medicalRecord.getBirthdate();

        int year = LocalDate.now().getYear();
        int getPersonBirthYear = Integer.parseInt(birthDate.split(":")[2]);

        return Integer.min(year, getPersonBirthYear);
    }
}
