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

    public MedicalRecordRepository(){
        loadMedicalRecords();
    }

    public void loadMedicalRecords() {
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

    public boolean addMedicalRecord(MedicalRecord medicalRecord) {
        save(medicalRecord);
        return true;
    }

    public void save(MedicalRecord medicalRecord) {
        medicalRecordList.add(medicalRecord);
    }
}
