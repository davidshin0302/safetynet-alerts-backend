package com.safetynet.alerts.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.service.CommunityEmailService;
import com.safetynet.alerts.service.FireResponseService;
import com.safetynet.alerts.service.PersonService;
import com.safetynet.alerts.view.FireResponse;
import com.safetynet.alerts.view.PersonInfoView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
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

    @Autowired
    private CommunityEmailService communityEmailService;

    @Autowired
    private FireResponseService fireResponseService;

    @GetMapping("/personInfo")
    public ResponseEntity<String> getPersonInfo(@RequestParam String firstName, @RequestParam String lastName) {
        ResponseEntity<String> responseEntity;
        List<PersonInfoView> personInfoViewList;

        log.info("...request handling /personInfo?firstName={}&lastName={}", firstName, lastName);

        try {
            personInfoViewList = personService.findPersonInfo(firstName, lastName);
            responseEntity = ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(objectMapper.writeValueAsString(personInfoViewList));

            log.info("processed /personInfo?firstName={}&lastName={} reuest...", firstName, lastName);
        } catch (IOException | RuntimeException ex) {
            log.error("Error Occurred while searching for {} {}.", firstName, lastName);
            responseEntity = new ResponseEntity<>("[AlertController:Line 46]: ", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return responseEntity;
    }

    @GetMapping("/communityEmail")
    public ResponseEntity<String> getCommunityEmail(@RequestParam String city) {
        ResponseEntity<String> responseEntity;
        List<String> communityEmailsList;

        log.info("...request handling /communityEmail?city={}", city);

        try {
            communityEmailsList = communityEmailService.findCommunityEmailsByCity(city);
            responseEntity = ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(objectMapper.writeValueAsString(communityEmailsList));

            log.info("processed /communityEmail?city={} request...", city);
        } catch (IOException | RuntimeException ex) {
            log.error("Error Occurred while retrieving community emails by {}.", city);
            responseEntity = new ResponseEntity<>("[AlertController:Line:68]: ", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return responseEntity;
    }

    @GetMapping("/fire")
    public ResponseEntity<String> getFireResponse(@RequestParam String address) {
        ResponseEntity<String> responseEntity;
        FireResponse fireResponse;

        log.info("...request handling /fire?address={}", address);

        try {
            fireResponse = fireResponseService.findFireResponse(address);
            responseEntity = ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(objectMapper.writeValueAsString(fireResponse));

            log.info("processed /fire?address={} request...", address);
        } catch (IOException | RuntimeException ex) {
            log.error("Error Occurred while retrieving fire response service from {}.", address);
            responseEntity = new ResponseEntity<>("[AlertController:Line:93]: ", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return responseEntity;
    }
}
