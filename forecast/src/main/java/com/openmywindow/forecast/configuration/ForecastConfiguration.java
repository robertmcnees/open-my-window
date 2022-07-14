package com.openmywindow.forecast.configuration;

import com.openmywindow.forecast.service.ForecastService;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ForecastConfiguration {

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}

	@Bean
	public ForecastService currentWeatherService(RestTemplate restTemplate) {
		return new ForecastService(restTemplate);
	}
}
