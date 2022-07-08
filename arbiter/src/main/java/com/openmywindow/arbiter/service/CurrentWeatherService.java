package com.openmywindow.arbiter.service;

import com.openmywindow.arbiter.record.CurrentWeatherRecord;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

public class CurrentWeatherService {

	private final RestTemplate restTemplate;

	@Value("${openweatherserviceurl")
	private String openWeatherServiceUrl;

	public CurrentWeatherService(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public CurrentWeatherRecord getCurrentWeather(Double lat, Double lon) {
		return restTemplate.getForObject("http://" + openWeatherServiceUrl + "/current/currentWeather?lat="+lat+"&lon="+lon, CurrentWeatherRecord.class);
	}
}
