package com.safetynet.alerts.view;

import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.MedicalRecordRepository;
import com.safetynet.alerts.repository.PersonRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;

//Might need to
@Getter
@Setter
@AllArgsConstructor
public class PersonInfoView {
    private String name;
    private String address;
    private String email;
    private int age;
    private List<String> medications;
    private List<String> allergies;

}
