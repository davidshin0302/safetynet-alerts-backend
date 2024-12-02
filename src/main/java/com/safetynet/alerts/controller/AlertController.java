package com.safetynet.alerts.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.service.PersonService;
import com.safetynet.alerts.view.CommunityEmail;
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

    @GetMapping("/personInfo")
    public ResponseEntity<String> getPersonInfo(@RequestParam String firstName, @RequestParam String lastName) throws IOException {
        ResponseEntity<String> responseEntity;

        List<PersonInfoView> personInfoViewList;

        try {
            personInfoViewList = personService.findPersonInfo(firstName, lastName);
            responseEntity = ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(objectMapper.writeValueAsString(personInfoViewList));
        } catch (RuntimeException ex) {
            log.error("Error Occurred while searching for {} {}", firstName, lastName);
            responseEntity = new ResponseEntity<>("[AlertController:Line 46]: ", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return responseEntity;
    }

    @GetMapping("/communityEmail")
    public ResponseEntity<List<CommunityEmail>> getCommunityEmail() {
        //TODO:: do something here..
    }
}
