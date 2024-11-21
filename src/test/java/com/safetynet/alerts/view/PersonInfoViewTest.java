package com.safetynet.alerts.view;

import com.safetynet.alerts.repository.MedicalRecordRepository;
import com.safetynet.alerts.repository.PersonRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;


import static org.junit.jupiter.api.Assertions.*;

@Disabled
@ExtendWith(SpringExtension.class)
class PersonInfoViewTest {
    private PersonInfoView personInfoView;
    private PersonRepository personRepository;
    private MedicalRecordRepository medicalRecordRepository;


    @Test
    void findAge() {
    }
}