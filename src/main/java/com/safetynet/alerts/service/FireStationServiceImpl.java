package com.safetynet.alerts.service;

import com.safetynet.alerts.model.FireStation;
import com.safetynet.alerts.repository.FireStationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FireStationServiceImpl implements FireStationService{

    @Autowired
    FireStationRepository fireStationRepository;

    @Override
    public FireStation saveFireStation(FireStation fireStation){
        return fireStationRepository.save(fireStation);
    }
}
