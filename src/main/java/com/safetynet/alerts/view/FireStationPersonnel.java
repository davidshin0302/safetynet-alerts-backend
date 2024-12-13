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
public class FireStationPersonnel {
    String stationNumber;
    List<OtherPersonInfo> otherPersonInfoList;
    String adultCount;
    String childCount;
}
