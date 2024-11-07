package com.safetynet.alerts.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Person {

    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    private String address;
    private String city;
    private String zip;
    private String phone;
    private String email;
//    private MedicalRecord medicalRecord;

    @Override
    public String toString() {
        return "Person{" +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", zip='" + zip + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Person person)) return false;

        return firstName.equalsIgnoreCase(person.firstName) && lastName.equalsIgnoreCase(person.lastName);
    }

    @Override
    public int hashCode() {
        int result = firstName.hashCode();
        result = 31 * result + lastName.hashCode();
        return result;
    }
}
