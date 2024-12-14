package com.safetynet.alerts.view;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OtherPersonInfo {
    String firstName;
    String lastName;
    int age;
    String address;
}
