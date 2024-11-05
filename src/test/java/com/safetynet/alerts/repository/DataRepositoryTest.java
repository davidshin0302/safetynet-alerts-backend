package com.safetynet.alerts.repository;


import lombok.var;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {DataRepository.class})
public class DataRepositoryTest {
    @Autowired
    private DataRepository dataRepository;

    @Test
    public void testGetPeople() {
        //TODO: to code
        //GIVEN
        var expected = 23;
        //WHEN
        var actual = dataRepository.getPeople();
        //THEN
        assertThat(actual.size()).isEqualTo(expected);
    }
}
