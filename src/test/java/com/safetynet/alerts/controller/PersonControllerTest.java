package com.safetynet.alerts.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.model.DataObject;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.PersonRepository;
import com.safetynet.alerts.service.PersonService;
import com.safetynet.alerts.view.PersonInfoView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(PersonController.class)
class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonRepository personRepository;

    @MockBean
    private PersonService personService;

    @InjectMocks
    private PersonController personController;

    private ObjectMapper objectMapper = new ObjectMapper();

    private static final String TEST_FILE_PATH = "src/test/resources";

    @BeforeEach
    public void setUp() throws IOException {
        when(personRepository.findAll()).thenReturn(objectMapper.readValue(new File(TEST_FILE_PATH + "/testData.json"), DataObject.class).getPersons());
    }

    @Test
    public void testGetPeople() throws Exception {
        mockMvc.perform(get("/person"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(23)));

        mockMvc.perform(get("/persons")).andExpect(status().isNotFound());
    }

    @Test
    public void testGetAll_exception() throws Exception {
        when(personRepository.findAll()).thenThrow(new RuntimeException("RuntimeException error"));

        mockMvc.perform(get("/person"))
                .andExpect(status().isInternalServerError());

    }

//    @Test
//    public void testGetPersonInfo() throws Exception {
//        List<PersonInfoView> personInfoViewList = objectMapper.readValue(new File(TEST_FILE_PATH + "/personDir/testPersonInfo.json"), new TypeReference<List<PersonInfoView>>(){});
//        when(personService.findPersonInfo(anyString(), anyString())).thenReturn(personInfoViewList);
//
//        mockMvc.perform(get("/personInfo?firstName=John&lastName=Boyd"))
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.[0].name").value("John Boyd"))
//                .andExpect(jsonPath("$.[0].address").value("1509 Culver St Culver, 97451"))
//                .andExpect(jsonPath("$.[0].email").value("jaboyd@email.com"))
//                .andExpect(jsonPath("$.[0].age").value(40))
//                .andExpect(jsonPath("$.[0].medications",hasSize(2)))
//                .andExpect(jsonPath("$.[0].allergies", hasSize(1)));
//    }

    @Test
    public void testGetPersonInfo_when_is_empty() throws Exception {
        List<PersonInfoView> personInfoViewList = new ArrayList<>();

        when(personService.findPersonInfo(anyString(), anyString())).thenReturn(personInfoViewList);

        mockMvc.perform(get("/personInfo?firstName=NoExist&lastName=NoExist"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testAddPerson() throws Exception {
        String newPersonFile = new String(Files.readAllBytes(Paths.get(TEST_FILE_PATH + "/personDir/testNewPerson.json")));
        Person testNewPerson = objectMapper.readValue(newPersonFile, Person.class);

        when(personRepository.save(testNewPerson)).thenReturn(true); //save new Person first
        when(personRepository.findPerson(any(String.class), any(String.class), any(String.class))).thenReturn(testNewPerson); //Return newly saved person from personList.

        mockMvc.perform(post("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newPersonFile))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.firstName").value("big"))
                .andExpect(jsonPath("$.lastName").value("head"))
                .andExpect(jsonPath("$.address").value("czz"))
                .andExpect(jsonPath("$.city").value("seoul"))
                .andExpect(jsonPath("$.zip").value("00000"))
                .andExpect(jsonPath("$.phone").value("000-000-0000"))
                .andExpect(jsonPath("$.email").value("bighead@email.com"));
    }

    @Test
    public void testAddPerson_conflict() throws Exception {
        Person person = new Person();
        person.setFirstName("David");
        person.setLastName("Shin");

        when(personRepository.save(person)).thenReturn(false);

        mockMvc.perform(post("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(person)))
                .andExpect(status().isConflict());
    }

    @Test
    public void testUpdateExistingPerson() throws Exception {
        String editPersonFile = new String(Files.readAllBytes(Paths.get(TEST_FILE_PATH + "/personDir/testEditPerson.json")));
        Person testEditPerson = objectMapper.readValue(editPersonFile, Person.class);

        when(personRepository.updateExistingPerson(any(Person.class))).thenReturn(true); // check if person exist in the list
        when(personRepository.findPerson(any(String.class), any(String.class),any(String.class))).thenReturn(testEditPerson); // After checked then add.

        mockMvc.perform(put("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(editPersonFile))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.firstName").value("Tessa"))
                .andExpect(jsonPath("$.lastName").value("Carman"))
                .andExpect(jsonPath("$.address").value("123 Main St"))
                .andExpect(jsonPath("$.city").value("Flushing"))
                .andExpect(jsonPath("$.zip").value("11304"))
                .andExpect(jsonPath("$.phone").value("841-874-6512"))
                .andExpect(jsonPath("$.email").value("tenz@email.com"));
    }

    @Test
    public void testUpdateExistingPerson_notFound() throws Exception {
        String newPersonJson = new String(Files.readAllBytes(Paths.get(TEST_FILE_PATH + "/personDir/testNewPerson.json")));
        Person person = objectMapper.readValue(newPersonJson, Person.class);

        when(personRepository.updateExistingPerson(person)).thenReturn(false);

        mockMvc.perform(put("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newPersonJson))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteExistingPerson() throws Exception {
        String newPersonFile = new String(Files.readAllBytes(Paths.get(TEST_FILE_PATH + "/personDir/testNewPerson.json")));
        Person deletePerson = objectMapper.readValue(newPersonFile, Person.class);

        when(personRepository.delete(any(String.class), any(String.class), any(String.class))).thenReturn(true); // Simulating successful deletion

        mockMvc.perform(delete("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(deletePerson)))
                .andExpect(status().isNoContent());


        when(personRepository.delete(any(String.class), any(String.class), any(String.class))).thenReturn(false); // Simulate failure to delete

        mockMvc.perform(delete("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(deletePerson)))
                .andExpect(status().isNotFound());

    }
}