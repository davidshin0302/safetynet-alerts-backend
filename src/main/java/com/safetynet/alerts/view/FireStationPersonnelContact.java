package com.safetynet.alerts.view;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FireStationPersonnelContact {
    String firstName;
    String lastName;
    String address;
    String phoneNumber;
}
