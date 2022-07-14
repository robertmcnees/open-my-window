package com.openmywindow.arbiter.config;

import com.openmywindow.arbiter.service.ForecastService;
import com.openmywindow.arbiter.service.GeocodeService;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;


@Configuration
public class ArbiterConfiguration {

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}

	@Bean
	public GeocodeService getGeocodeService(RestTemplate restTemplate) {
		return new GeocodeService(restTemplate);
	}

	@Bean
	public ForecastService getCurrentWeatherService(RestTemplate restTemplate) { return new ForecastService(restTemplate); }
}
