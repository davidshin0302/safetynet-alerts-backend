package com.safetynet.alerts.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Person {
    public Person() {
    }

    private String firstName;
    private String lastName;
    private String address;
    private String city;
    private String zip;
    private String phone;
    private String email;

}
