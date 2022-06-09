package com.openmywindow.geocode.configuration;

import com.openmywindow.geocode.repository.GeocodeRepository;
import com.openmywindow.geocode.service.GeocodeService;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class GeocodeConfiguration {

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}

	@Bean
	public GeocodeService geocodeService(RestTemplate restTemplate, OpenWeatherVaultConfiguration openWeatherVaultConfiguration, GeocodeRepository geocodeRepository) {
		return new GeocodeService(restTemplate, openWeatherVaultConfiguration, geocodeRepository);
	}
}
