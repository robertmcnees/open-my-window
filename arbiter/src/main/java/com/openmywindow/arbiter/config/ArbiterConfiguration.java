package com.openmywindow.arbiter.config;

import com.openmywindow.arbiter.service.MockWeatherService;
import com.openmywindow.arbiter.service.WeatherService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class ArbiterConfiguration {

	@Bean
	public WeatherService mockWeatherService() {
		return new MockWeatherService();
	}

}
