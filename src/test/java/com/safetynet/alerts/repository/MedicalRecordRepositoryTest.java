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

    private static final String TEST_FILE_PATH = "src/test/resources";

    @BeforeEach
    void setUp() throws IOException {
        medicalRecordRepository = new MedicalRecordRepository();
        medicalRecordList = objectMapper.readValue(new File(TEST_FILE_PATH + "/testData.json"), DataObject.class).getMedicalRecords();
    }

    @Test
    void findAll() {
        assertNotNull(medicalRecordList);
        assertEquals(23, medicalRecordList.size());

        //{ "firstName":"John", "lastName":"Boyd", "birthdate":"03/06/1984", "medications":["aznol:350mg", "hydrapermazol:100mg"], "allergies":["nillacilan"] },
        var medicalRecord = medicalRecordList.get(0);
        assertEquals("John", medicalRecord.getFirstName());
        assertEquals("Boyd", medicalRecord.getLastName());

        var regex = "^\\d{2}/\\d{2}/\\d{4}$";
        assertTrue(medicalRecord.getBirthdate().matches(regex));

        var medications = medicalRecord.getMedications();
        var expected1 = medications.get(0).split(":");
        assertEquals("aznol", expected1[0]);
        assertEquals("350mg", expected1[1]);

        assertEquals(2, medicalRecord.getMedications().size());
        assertEquals(1, medicalRecord.getAllergies().size());

        var allergies = medicalRecord.getAllergies();
        assertEquals("nillacilan", allergies.get(0));
    }

    @Test
    void addMedicalRecord() {
    }

    @Test
    void save() {
    }
}