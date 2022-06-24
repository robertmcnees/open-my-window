package com.openmywindow.arbiter.service;

import com.openmywindow.arbiter.record.CurrentWeatherRecord;

import org.springframework.web.client.RestTemplate;

public class CurrentWeatherService {

	private final RestTemplate restTemplate;

	public CurrentWeatherService(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public CurrentWeatherRecord getCurrentWeather(Double lat, Double lon) {
		return restTemplate.getForObject("http://localhost:8082/current/currentWeather?lat="+lat+"&lon="+lon, CurrentWeatherRecord.class);
	}
}
