package com.safetynet.alerts.controller;

import com.safetynet.alerts.repository.PersonRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PersonController.class)
class PersonControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonRepository personRepository;

    @Test
    void getPeople() throws Exception {
        // Arrange
        mockMvc.perform(get("/person")) .andExpect(status().isOk());
        // Act
        // Assert
    }

    @Test
    void addPerson() {
        // Arrange
        // Act
        // Assert
    }

    @Test
    void updateExistingPerson() {
        // Arrange
        // Act
        // Assert
    }

    @Test
    void deleteExistingPerson() {
        // Arrange
        // Act
        // Assert
    }
}