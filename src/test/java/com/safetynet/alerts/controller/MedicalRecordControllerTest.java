package com.safetynet.alerts.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.model.DataObject;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.repository.MedicalRecordRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.io.File;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MedicalRecordController.class)
class MedicalRecordControllerTest {

    private static final String TEST_FILE_PATH = "src/test/resources";
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private MedicalRecordRepository medicalRecordRepository;
    private MedicalRecordController medicalRecordController;

    @Test
    void getAllMedicalRecords() throws Exception {
        when(medicalRecordRepository.findAll()).thenReturn(objectMapper.readValue(new File(TEST_FILE_PATH + "/testData.json"), DataObject.class).getMedicalRecords());

        mockMvc.perform(get("/medicalRecord").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(23)));

    }

    @Test
    void getAllMedicalRecords_test_runtimeException() throws Exception {
        when(medicalRecordRepository.findAll()).thenThrow(new RuntimeException("RuntimeException error"));

        mockMvc.perform(get("/medicalRecord"))
                .andExpect(status().isInternalServerError());  // Expecting HTTP status 500 Internal Server Error
    }


    @Test
    void addMedicalRecord() throws Exception {
        MedicalRecord medicalRecord = objectMapper.readValue(new File(TEST_FILE_PATH + "/medicalRecordDir/testNewMedicalRecord.json"), MedicalRecord.class);
        when(medicalRecordRepository.save(any(MedicalRecord.class))).thenReturn(true);

        mockMvc.perform(post("/medicalRecord").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(medicalRecord)))
                .andExpect(status().isCreated());
    }

    @Test
    void addMedicalRecord_conflict() throws Exception {
        MedicalRecord medicalRecord = new MedicalRecord();
        when(medicalRecordRepository.save(any(MedicalRecord.class))).thenReturn(false);

        mockMvc.perform(post("/medicalRecord").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(medicalRecord)))
                .andExpect(status().isConflict());
    }

    @Test
    void updateMedicalRecord() throws Exception {
        MedicalRecord medicalRecord = objectMapper.readValue(new File(TEST_FILE_PATH + "/medicalRecordDir/testUpdateMedicalRecrod.json"), MedicalRecord.class);
        when(medicalRecordRepository.updateExistingMedicalRecord(any(MedicalRecord.class))).thenReturn(true);

        mockMvc.perform(put("/medicalRecord").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(medicalRecord)))
                .andExpect(status().isCreated());
    }

    @Test
    void updateMedicalRecord_not_found() throws Exception {
        MedicalRecord medicalRecord = new MedicalRecord();
        when(medicalRecordRepository.updateExistingMedicalRecord(any(MedicalRecord.class))).thenReturn(false);

        mockMvc.perform(put("/medicalRecord").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(medicalRecord)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteMedicalRecord() throws Exception {
        MedicalRecord medicalRecord = objectMapper.readValue(new File(TEST_FILE_PATH + "/medicalRecordDir/testNewMedicalRecord.json"), MedicalRecord.class);
        when(medicalRecordRepository.delete(any(MedicalRecord.class))).thenReturn(true);

        mockMvc.perform(delete("/medicalRecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(medicalRecord)))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteMedicalRecord_not_found() throws Exception {
        MedicalRecord medicalRecord = new MedicalRecord();
        when(medicalRecordRepository.delete(any(MedicalRecord.class))).thenReturn(false);

        mockMvc.perform(delete("/medicalRecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(medicalRecord)))
                .andExpect(status().isNotFound());
    }
}