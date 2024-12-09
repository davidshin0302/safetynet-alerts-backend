package com.safetynet.alerts.view;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChildAlertResponse {
    Set<ChildInfo> children;
    Set<OtherPersonInfo> otherPersons;
}
