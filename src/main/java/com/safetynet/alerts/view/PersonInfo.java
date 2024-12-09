package com.safetynet.alerts.view;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PersonInfo {
    private String name;
    private String address;
    private String email;
    private int age;
    private List<String> medications;
    private List<String> allergies;

}
