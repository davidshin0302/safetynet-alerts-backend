package com.safetynet.alerts.repository;

import com.safetynet.alerts.model.Person;
import org.springframework.data.repository.CrudRepository;

public interface personRepository extends CrudRepository<Person, Long> {
}
