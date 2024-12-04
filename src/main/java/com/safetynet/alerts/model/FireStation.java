package com.safetynet.alerts.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FireStation {

    private String address;
    private String station;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FireStation fireStation)) return false;

        return address.equals(fireStation.getAddress()) && station.equals(fireStation.getStation());
    }

    @Override
    public int hashCode() {
        int result = address.hashCode();
        result = 31 * result + station.hashCode();
        return result;
    }
}
