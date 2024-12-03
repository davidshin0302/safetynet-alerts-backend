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
public class FireResponse {
    private String address; //TODO:: delete it.
    private String fireStationNumber;
    private List<Resident> residents;
}
