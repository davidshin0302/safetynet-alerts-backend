package com.safetynet.alerts.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.model.DataObject;
import com.safetynet.alerts.model.MedicalRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class MedicalRecordRepositoryTest {

    private MedicalRecordRepository medicalRecordRepository;
    private ObjectMapper objectMapper = new ObjectMapper();
    private List<MedicalRecord> medicalRecordList;
    private String regex;

    private static final String TEST_FILE_PATH = "src/test/resources";

    @BeforeEach
    void setUp() throws IOException {
        regex = "^\\d{2}/\\d{2}/\\d{4}$"; // Format checking MM/DD/YYYY
        medicalRecordRepository = new MedicalRecordRepository();
        medicalRecordList = objectMapper.readValue(new File(TEST_FILE_PATH + "/testData.json"), DataObject.class).getMedicalRecords();
    }

    @Test
    void findAll() {
        MedicalRecordRepository tempMedicalRecordRepository = new MedicalRecordRepository();
        medicalRecordList = tempMedicalRecordRepository.findAll();

        //Test is list is not empty.
        assertNotNull(medicalRecordList);
        assertEquals(23, medicalRecordList.size());

        //Test first and last name.
        var medicalRecord = medicalRecordList.get(0);
        assertEquals("John", medicalRecord.getFirstName());
        assertEquals("Boyd", medicalRecord.getLastName());

        //Test for birthDate format MM/DD/YYYY
        assertTrue(medicalRecord.getBirthdate().matches(regex));

        //Test for medication list.
        var medications = medicalRecord.getMedications();
        var expected1 = medications.get(0).split(":");
        assertEquals("aznol", expected1[0]);
        assertEquals("350mg", expected1[1]);

        //Test for medications and allergies are not empty.
        assertEquals(2, medicalRecord.getMedications().size());
        assertEquals(1, medicalRecord.getAllergies().size());

        //Test for allergy list.
        var allergies = medicalRecord.getAllergies();
        assertEquals("nillacilan", allergies.get(0));
    }

    @Test
    void findMedicalRecordByFirstLastName() throws IOException {
        var medicalRecordJson = "{ \"firstName\":\"John\", \"lastName\":\"Boyd\", \"birthdate\":\"03/06/1984\", \"medications\":[\"aznol:350mg\", \"hydrapermazol:100mg\"], \"allergies\":[\"nillacilan\"]};";
        var medicalRecord = objectMapper.readValue(medicalRecordJson, MedicalRecord.class);

        //Both conditions are true.
        var expectedMedicalRecord = medicalRecordRepository.findMedicalRecordByFirstLastName(medicalRecord.getFirstName(), medicalRecord.getLastName());
        assertNotNull(medicalRecord);
        assertEquals(medicalRecord.getFirstName(), expectedMedicalRecord.getFirstName());
        assertEquals(medicalRecord.getLastName(), expectedMedicalRecord.getLastName());
        assertEquals(2, medicalRecord.getMedications().size());
        assertEquals(1, medicalRecord.getAllergies().size());

        //First condition is false
        assertNull(medicalRecordRepository.findMedicalRecordByFirstLastName("wrong first name", medicalRecord.getLastName()));
        //First condition is true, second is false
        assertNull(medicalRecordRepository.findMedicalRecordByFirstLastName(medicalRecord.getFirstName(), "wrong last name"));
        //Both condition is false
        assertNull(medicalRecordRepository.findMedicalRecordByFirstLastName("wrong first name", "wrong last name"));
    }

    @Test
    void save() throws IOException {
        var medicalRecordJson = "{ \"firstName\":\"David\", \"lastName\":\"Shin\", \"birthdate\":\"03/02/1987\", \"medications\":[\"Tylenol:1000mg\", \"Migraine:2000mg\"], \"allergies\":[\"Crab\", \"shellfish\", \"apple\"] }";
        var medicalRecord = objectMapper.readValue(medicalRecordJson, MedicalRecord.class);

        assertTrue(medicalRecordRepository.save(medicalRecord));

        //Attempt to save same medicalRecord.
        assertFalse(medicalRecordRepository.save(medicalRecord));
    }
}