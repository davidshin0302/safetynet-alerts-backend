package com.safetynet.alerts.service;

import com.safetynet.alerts.view.PersonInfoView;
import jakarta.validation.constraints.NotBlank;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResponderService {

    public List<PersonInfoView> findPersonInfo(@NotBlank String firstName, @NotBlank String lastName) {
        return null;
    }
}
