package com.safetynet.alerts.service;

import com.safetynet.alerts.model.MedicalRecord;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface MedicalRecordService {
    ResponseEntity<List<MedicalRecord>> getALlMedicalRecords();
//    ResponseEntity<HttpStatus> addNewMedicalRecord(MedicalRecord medicalRecord);
}
