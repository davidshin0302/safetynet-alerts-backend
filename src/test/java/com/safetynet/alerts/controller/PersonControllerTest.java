package com.safetynet.alerts.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.model.DataObject;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(PersonController.class)
class PersonControllerTest {
    @Autowired
    private MockMvc mockMvc;

    //PersonController has dependency of personRepository thus Mocking is need it.
    @MockBean
    private PersonRepository personRepository;

    private String personJsonList;

    private String filePath = "src/test/resources";


    @BeforeEach
    public void setUp() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Person> personList = objectMapper.readValue(new File(filePath + "/testData.json"), DataObject.class).getPersons();

        Person testNewPerson = objectMapper.readValue(new File(filePath + "/testNewPerson.json"), Person.class);
        Person testEditPerson = objectMapper.readValue(new File(filePath + "/testEditPerson.json"), Person.class);
        personJsonList = objectMapper.writeValueAsString(personList);


        //Mock the behavior of PersonRepository in PersonController.
        when(personRepository.findAll()).thenReturn(personList);
        when(personRepository.findByFirstAndLastName(testNewPerson)).thenReturn(testNewPerson);
        when(personRepository.findByFirstAndLastName(testEditPerson)).thenReturn(testEditPerson);
        when(personRepository.updateExistingPerson(testEditPerson)).thenReturn(true);
        when(personRepository.save(any(Person.class))).thenReturn(true);
    }

    @Test
    public void testGetPeople() throws Exception {
        mockMvc.perform(get("/person"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(personJsonList));

        mockMvc.perform(get("/persons")).andExpect(status().isNotFound());
    }

    @Test
    public void testAddPerson() throws Exception {
        String newPersonFile = new String(Files.readAllBytes(Paths.get(filePath+ "/testNewPerson.json")));


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
    public void testUpdateExistingPerson() throws Exception {
        String editPersonFile  = new String(Files.readAllBytes(Paths.get(filePath+ "/testEditPerson.json")));

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
    public void testDeleteExistingPerson() {
        // Arrange
        // Act
        // Assert
    }
}