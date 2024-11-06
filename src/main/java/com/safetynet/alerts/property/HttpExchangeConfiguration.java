package com.safetynet.alerts.property;

import org.springframework.boot.actuate.web.exchanges.HttpExchangeRepository;
import org.springframework.boot.actuate.web.exchanges.InMemoryHttpExchangeRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/*
 * The `/actuator/trace` endpoint was deprecated in Spring Boot 2.2.0 and later.
 * It's no longer recommended due to potential security risks and bugs.
 *
 * The recommended alternative is the `/actuator/httpexchange` endpoint.
 * This endpoint provides a safer and more controlled way to access HTTP exchange information.
 *
 * I had to use a custom HttpExchangeRepository because the default settings weren't working correctly for the /actuator/httpexchange endpoint.
 * This can be done by creating a bean of type `HttpExchangeRepository` and returning an instance of `InMemoryHttpExchangeRepository`.
 * Reference: https://stackoverflow.com/questions/59115578/httptrace-endpoint-of-spring-boot-actuator-doesnt-exist-anymore-with-spring-b
 */

@Configuration
public class HttpExchangeConfiguration {

    @Bean
    public HttpExchangeRepository createTraceRepository() {
        return new InMemoryHttpExchangeRepository();
    }
}