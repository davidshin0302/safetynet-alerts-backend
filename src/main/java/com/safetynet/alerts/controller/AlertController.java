package com.safetynet.alerts.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.service.*;
import com.safetynet.alerts.view.ChildInfo;
import com.safetynet.alerts.view.FireResponse;
import com.safetynet.alerts.view.FloodResponse;
import com.safetynet.alerts.view.PersonInfo;
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
import java.util.Map;


@RestController
@Slf4j
public class AlertController {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PersonInfoService personInfoService;

    @Autowired
    private CommunityEmailService communityEmailService;

    @Autowired
    private FireResponseService fireResponseService;

    @Autowired
    private FloodResponseService floodResponseService;

    @Autowired
    private ChildAlertResponseService childAlertResponseService;

    /**
     * Retrieves person information based on provided first and last name.
     *
     * @param firstName The first name of the person to search for.
     * @param lastName The last name of the person to search for.
     * @return A ResponseEntity containing the person information as JSON or an error message
     *         depending on the success of the operation. The status code will be set accordingly:
     *         - `200 OK`: Person information found.
     *         - `500 INTERNAL_SERVER_ERROR`: An error occurred while processing the request.
     * @throws RuntimeException,IOException If either `firstName` or `lastName` is null or empty.
     */
    @GetMapping("/personInfo")
    public ResponseEntity<String> getPersonInfo(@RequestParam String firstName, @RequestParam String lastName) {
        ResponseEntity<String> responseEntity;
        List<PersonInfo> personInfoList;

        log.info("...request handling /personInfo?firstName={}&lastName={}", firstName, lastName);

        try {
            personInfoList = personInfoService.findPersonInfo(firstName, lastName);
            responseEntity = ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(objectMapper.writeValueAsString(personInfoList));

            log.info("processed /personInfo?firstName={}&lastName={} reuest...", firstName, lastName);
        } catch (IOException | RuntimeException ex) {
            log.error("Error Occurred while searching for {} {}.", firstName, lastName);
            log.error(ex.getMessage());

            responseEntity = new ResponseEntity<>("[AlertController:Line 46]: ", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return responseEntity;
    }

    /**
     * Retrieves a list of community emails for a given city.
     *
     * @param city The city for which to retrieve community emails.
     * @return A ResponseEntity containing a list of community emails as JSON or an error message
     *         depending on the success of the operation. The status code will be set accordingly:
     *         - `200 OK`: Community emails found for the city.
     *         - `500 INTERNAL_SERVER_ERROR`: An error occurred while processing the request.
     * @throws RuntimeException,IOException If `city` is null or empty.
     */
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
            log.error(ex.getMessage());

            responseEntity = new ResponseEntity<>("[AlertController:Line:68]: ", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return responseEntity;
    }

    /**
     * Retrieves fire response information for a given address.
     *
     * @param address The address for which to retrieve fire response information.
     * @return A ResponseEntity containing fire response details as JSON or an error message
     *         depending on the success of the operation. The status code will be set accordingly:
     *         - `200 OK`: Fire response information found for the address.
     *         - `500 INTERNAL_SERVER_ERROR`: An error occurred while processing the request.
     * @throws RuntimeException,IOException  If `address` is null or empty.
     */
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
            log.error(ex.getMessage());

            responseEntity = new ResponseEntity<>("[AlertController:Line:93]: ", HttpStatus.INTERNAL_SERVER_ERROR);

        }

        return responseEntity;
    }
/**
 * Retrieves flood response information for a list of fire stations.
 *
 * @param stations A list of fire station IDs for which to retrieve flood response information.
 * @return A ResponseEntity containing:
 *         - A JSON representation of the flood response details mapped by station if the operation is successful.
 *         - An error response with a status code if an issue occurs.
 *
 *         The status codes include:
 *         - `200 OK`: Flood response information successfully retrieved.
 *         - `500 INTERNAL_SERVER_ERROR`: An error occurred while processing the request.
 * @throws RuntimeException If an unexpected runtime exception occurs during processing.
 * @throws IOException If an error occurs while serializing the flood response data to JSON.
 */
 @GetMapping("/flood/stations")
    public ResponseEntity<String> getFloodResponse(@RequestParam List<String> stations){
        ResponseEntity<String> responseEntity;
        Map<String, List<FloodResponse>> floodResponseMap;

        log.info("...request handling /flood/stations={}", stations);

        try{
            floodResponseMap = floodResponseService.findFloodResponse(stations);
            responseEntity = ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(objectMapper.writeValueAsString(floodResponseMap));

            log.info("processed /flood/stations={}", stations);
        } catch (IOException | RuntimeException ex) {
            log.error("Error Occurred while retrieving flood response service from the list: {}.", stations);
            log.error(ex.getMessage());

            responseEntity = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return  responseEntity;
    }

    @GetMapping("/childAlert")
    public ResponseEntity<String> getChildAlert(@RequestParam String address){
        ResponseEntity<String> responseEntity;


        log.info("...request handling /childAlert?address={}", address);

        try{
            List<ChildInfo> childInfoList = childAlertResponseService.findChildAlert(address);
            String result = objectMapper.writeValueAsString(childInfoList);

            if(childInfoList.isEmpty()){
                result = " ";
            }

            responseEntity = ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(result);
        } catch (IOException | RuntimeException ex) {
            log.error("Error Occurred while retrieving child alert  service from the address: {}.", address);
            log.error(ex.getMessage());

            responseEntity = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return  responseEntity;
    }
}
