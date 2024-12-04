package com.safetynet.alerts.service;

import com.safetynet.alerts.view.FireResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class FloodResponseService {
    @Autowired
    private FireResponseService fireResponseService;

    private Map<String, FireResponse> floodResponseMap;

    public Map<String, FireResponse> findFloodResponse(List<String> stations){
        return floodResponseMap;
    }
    /*
    TODO:: Implement flood station response service based on this json object.
    {
  "stations": [
    {
      "stationNumber": 1,
      "households": [
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
