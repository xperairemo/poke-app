package com.poketactics.apppoke.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration // Le dice a Spring que aquí hay configuraciones importantes
public class RestTemplateConfig {

    @Bean // Registra RestTemplate en el "cajón" de herramientas de Spring
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}