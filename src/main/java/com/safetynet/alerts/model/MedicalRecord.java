package com.safetynet.alerts.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MedicalRecord {

    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    private String birthdate;
    private List<String> medications;
    private List<String> allergies;

    @Override
    public String toString() {
        return "MedicalRecord{" +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", birthdate='" + birthdate + '\'' +
                ", medications=" + medications +
                ", allergies=" + allergies +
                '}';
    }

    @JsonIgnore
    public String getUniqueIdentifier() {
        return firstName + "_" + lastName;
    }
}