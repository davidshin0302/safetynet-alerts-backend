package com.safetynet.alerts.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class MedicalRecord {
    public MedicalRecord(){}

    private String firstName;
    private String lastName;
    private String birthdate;
    private ArrayList<String> medication;
    private ArrayList<String> allergies;
}
