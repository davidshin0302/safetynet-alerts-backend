package com.safetynet.alerts.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DataObject {
    private List<Person> persons;
    @JsonProperty("firestations")
    private List<FireStation> fireStations;
    @JsonProperty("medicalrecords")
    private List<MedicalRecord> medicalRecords;
}
