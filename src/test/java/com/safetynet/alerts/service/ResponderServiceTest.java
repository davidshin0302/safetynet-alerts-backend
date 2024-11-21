package com.safetynet.alerts.service;

import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.FireStationRepository;
import com.safetynet.alerts.repository.PersonRepository;
import com.safetynet.alerts.view.PersonInfoView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class ResponderServiceTest {
    @Autowired
    ResponderService responderService;

    @MockBean
    PersonRepository personRepository;

    //TODO: Add mock repository, and train it.
    @Test
    public void findPersonInfo(){
        Person person = new Person("david", "shin", "123 main st", "NYC", "NY","12345", "david@email.com");
        MedicalRecord medicalRecord = new MedicalRecord("david", "shin", "03/02/1987", List.of("123"), List.of("ABC"));

        PersonInfoView expected = new PersonInfoView(person, medicalRecord);

        //Pass parameter from controller.
        List<PersonInfoView> actual = responderService.findPersonInfo(person.getFirstName(), person.getLastName());

        //TODO: add assertion, actual = expected.
    }
}
