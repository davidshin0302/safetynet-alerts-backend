package com.safetynet.alerts.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.repository.MedicalRecordRepository;
import com.safetynet.alerts.repository.PersonRepository;
import com.safetynet.alerts.service.PersonService;
import com.safetynet.alerts.view.PersonInfoView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * TODO:: Implement below request handler.
 * /childAlert?address=<address> (GET): Retrieve children at an address and their household members.
 * /fire?address=<address> (GET): Get fire station servicing an address and people living there.
 */
@RestController
@Slf4j
public class AlertController {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PersonService personService;

    @GetMapping(value = "/personInfo")
    public ResponseEntity<String> getPersonInfo(@RequestParam String firstName, @RequestParam String lastName) throws JsonProcessingException {
        List<PersonInfoView> personInfoViewList = personService.findPersonInfo(firstName, lastName);

        if (!personInfoViewList.isEmpty()) {
            String personInfoViewListToJson = objectMapper.writeValueAsString(personInfoViewList);

            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(personInfoViewListToJson);
        } else {
            log.info("Unable to find data from first name: {} and last name: {}.", firstName, lastName);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
