package com.safetynet.alerts.service;

import com.safetynet.alerts.view.FireResponse;
import com.safetynet.alerts.view.FloodResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class FloodResponseService {
    @Autowired
    private FireResponseService fireResponseService;


    public Map<String, List<FloodResponse>> findFloodResponse(List<String> stations) {
        Map<String, FloodResponse> floodResponseMap = loadFloodResponseService();
        Map<String, List<FloodResponse>> resultMap = new HashMap<>();
        String key = "stations";

        resultMap.put(key, new ArrayList<>());

        for (String station : stations) {

            if (floodResponseMap.containsKey(station)) {
                List<FloodResponse> floodResponseList = resultMap.get(key);
                FloodResponse floodResponse = floodResponseMap.get(station);

                floodResponseList.add(floodResponse);
            }
        }
        
        return resultMap;
    }

    private Map<String, FloodResponse> loadFloodResponseService() {
        Map<String, FloodResponse> floodResponseMap = new HashMap<>();
        Map<String, FireResponse> fireResponseMap = fireResponseService.getFireResponse();

        for (FireResponse fireResponse : fireResponseMap.values()) {
            String station = fireResponse.getFireStationNumber();

            if (floodResponseMap.containsKey(station)) {
                FloodResponse floodResponse = floodResponseMap.get(station);
                List<FireResponse> fireResponseList = floodResponse.getHouseholds();

                fireResponseList.add(fireResponse);
                floodResponse.setHouseholds(fireResponseList);
            } else {
                floodResponseMap.put(station, new FloodResponse());
                FloodResponse floodResponse = floodResponseMap.get(station);
                floodResponse.setStationNumber(station);
                floodResponse.setHouseholds(new ArrayList<>());
            }
        }

        return floodResponseMap;
    }
    /*
    TODO:: Implement flood station response service based on this json object.
    {
  "3": [
    {
      "stationNumber": 1,
      "households(FireReSponse)": [
        {
          "address": "123 Main St",
          "residents": [
            {
              "name": "John Doe",
              "phone": "555-1234",
              "age": 34,
              "medications": ["ibuprofen:200mg"],
              "allergies": ["peanuts"]
            },
            {
              "name": "Jane Doe",
              "phone": "555-1234",
              "age": 30,
              "medications": ["acetaminophen:500mg"],
              "allergies": []
            }
          ]
        }
      ]
    }
  ]
}

     */
}
