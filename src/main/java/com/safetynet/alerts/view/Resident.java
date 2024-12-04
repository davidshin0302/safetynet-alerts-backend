package com.safetynet.alerts.view;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Resident {
    private String name;
    private String phone;
    private int age;
    private MedicalInfo medicalInfo;

}
