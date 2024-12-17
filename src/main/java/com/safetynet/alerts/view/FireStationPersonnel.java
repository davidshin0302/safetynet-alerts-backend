package com.safetynet.alerts.view;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FireStationPersonnel {
    String stationNumber;
    List<FireStationPersonnelContact> fireStationPersonnelContacts;
    int adultCount;
    int childCount;
}
