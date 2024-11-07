package com.safetynet.alerts.controller;

import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.service.MedicalRecordService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/medicalRecord")
public class MedicalRecordController {
//TODO:: one to one relationship between Person model.


//    @Autowired
//    MedicalRecordService medicalRecordService;
//
//    @GetMapping
//    public ResponseEntity<List<MedicalRecord>> getALlMedicalRecords(){
//        return medicalRecordService.getALlMedicalRecords();
//    }

//    @PostMapping
//    public ResponseEntity<HttpStatus> createMedicalRecords(@Valid @RequestBody MedicalRecord medicalRecord){
//        return medicalRecordService.addNewMedicalRecord(medicalRecord);
//    }
}
