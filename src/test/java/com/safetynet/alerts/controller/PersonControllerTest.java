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
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(PersonController.class)
class PersonControllerTest {
    @Autowired
    private MockMvc mockMvc;

    //PersonController has dependency of personRepository thus Mocking is need it.
    @MockBean
    PersonRepository personRepository;

    private String personJsonList;

    private String testFilePath = "src/test/resources/";

    @BeforeEach
    public void setUp() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Person> personList = objectMapper.readValue(new File(testFilePath + "testData.json"), DataObject.class).getPersons();
        personJsonList = objectMapper.writeValueAsString(personList);

        //Mock the behavior of PersonRepository in PersonController.
        when(personRepository.findAll()).thenReturn(personList);
        when(personRepository.save(any(Person.class))).thenReturn(true);
    }

    @Test
    public void getPeople() throws Exception {
        mockMvc.perform(get("/person"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(personJsonList));

        mockMvc.perform(get("/persons")).andExpect(status().isNotFound());
    }

    @Test
    public void addPerson() throws Exception {
        String newPerson = "{ \"firstName\":\"big\", \"lastName\":\"head\", \"address\":\"czz\", \"city\":\"seoul\", \"zip\":\"00000\", \"phone\":\"000-000-0000\", \"email\":\"bighead@email.com\" }";

        mockMvc.perform(post("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newPerson))
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
    public void updateExistingPerson() {
        // Arrange
        // Act
        // Assert
    }

    @Test
    public void deleteExistingPerson() {
        // Arrange
        // Act
        // Assert
    }
}