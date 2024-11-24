package com.safetynet.alerts.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.model.DataObject;
import com.safetynet.alerts.model.MedicalRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Repository
@Slf4j
public class MedicalRecordRepository {

    private final List<MedicalRecord> medicalRecordList = new ArrayList<>();

    public MedicalRecordRepository() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String filePath = "src/main/resources/data.json";
            DataObject dataObject = objectMapper.readValue(new File(filePath), DataObject.class);
            medicalRecordList.addAll(dataObject.getMedicalRecords());
        } catch (IOException | RuntimeException ex) {
            throw new RuntimeException(ex);
        }
    }

    public List<MedicalRecord> findAll() {
        return medicalRecordList;
    }

    /**
     * Returning the reference the object that found. **Hashcode(object Id) try to use debug mode.
     *
     * @param firstName
     * @param lastName
     * @return refernce of MedicalRecord Object or else null.
     */
    //Helper method, Not access to outside
    private MedicalRecord findMedicalRecordByFirstLastName(String firstName, String lastName) {
        //Returning the reference the object that found. **Hashcode(object Id) try to use debug mode.
        //Primitives are return original but other return by reference.
        return medicalRecordList.stream()
                .filter(medicalRecord -> medicalRecord.getFirstName().equalsIgnoreCase(firstName) && medicalRecord.getLastName().equalsIgnoreCase(lastName))
                .findFirst()
                .orElse(null);
    }

    //Expose private method through a public wrapper
    public MedicalRecord findRecord(String firstname, String lastName) {
        return findMedicalRecordByFirstLastName(firstname, lastName);
    }

    public boolean updateExistingMedicalRecord(MedicalRecord medicalRecord) {
        boolean result = false;

        MedicalRecord existingMedicalRecord = findMedicalRecordByFirstLastName(medicalRecord.getFirstName(), medicalRecord.getLastName());

        if (existingMedicalRecord != null) {
            existingMedicalRecord.setBirthdate(medicalRecord.getBirthdate());
            existingMedicalRecord.setMedications(medicalRecord.getMedications());
            existingMedicalRecord.setAllergies(medicalRecord.getAllergies());

            result = true;
        } else {
            log.info("Unable to find the matching medical record: {}", medicalRecord);
        }

        return result;
    }

    public boolean save(MedicalRecord medicalRecord) {
        boolean result = false;

        if (findMedicalRecordByFirstLastName(medicalRecord.getFirstName(), medicalRecord.getLastName()) == null) {
            result = medicalRecordList.add(medicalRecord);
        } else {
            log.info("The medicalRecord already exist from the data.");
        }
        return result;
    }

    public boolean delete(MedicalRecord medicalRecord) {
        boolean result = false;
        MedicalRecord deleteMedicalRecord = findMedicalRecordByFirstLastName(medicalRecord.getFirstName(), medicalRecord.getLastName());

        if (deleteMedicalRecord != null) {
            result = medicalRecordList.remove(deleteMedicalRecord);
        }
        return result;
    }
}
