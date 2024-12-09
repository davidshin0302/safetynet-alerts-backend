package com.safetynet.alerts.view;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChildAlertResponse {
    List<ChildInfo> children;
    List<OtherPersonInfo> otherPersons;
}
