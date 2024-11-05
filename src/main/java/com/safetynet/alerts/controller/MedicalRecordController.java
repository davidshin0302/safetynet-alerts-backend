package com.safetynet.alerts.controller;

import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.service.MedicalRecordServiceImpl;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/medicalRecord")
public class MedicalRecordController {

    @Autowired
    MedicalRecordServiceImpl medicalRecordService;

    @GetMapping
    public ResponseEntity<List<MedicalRecord>> getALlMedicalRecords(){
        return medicalRecordService.getALlMedicalRecords();
    }

//    @PostMapping
//    public ResponseEntity<HttpStatus> createMedicalRecords(@Valid @RequestBody MedicalRecord medicalRecord){
//        return medicalRecordService.addNewMedicalRecord(medicalRecord);
//    }
}
