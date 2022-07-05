package com.openmywindow.currentweather.configuration;

import com.openmywindow.currentweather.service.CurrentWeatherService;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class CurrentWeatherConfiguration {

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}

	@Bean
	public CurrentWeatherService currentWeatherService(RestTemplate restTemplate, OpenWeatherVaultConfiguration openWeatherVaultConfiguration) {
		return new CurrentWeatherService(restTemplate, openWeatherVaultConfiguration);
	}
}
