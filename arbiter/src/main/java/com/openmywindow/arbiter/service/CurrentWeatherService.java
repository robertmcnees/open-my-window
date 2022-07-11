package com.openmywindow.arbiter.service;

import com.openmywindow.arbiter.record.CurrentWeatherRecord;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

public class CurrentWeatherService {

	private final RestTemplate restTemplate;

	@Value("${currentweatherserviceurl:omw-currentweather}")
	private String currentWeatherServiceUrl;

	public CurrentWeatherService(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public CurrentWeatherRecord getCurrentWeather(Double lat, Double lon) {
		return restTemplate.getForObject("http://" + currentWeatherServiceUrl + "/current/currentWeather?lat="+lat+"&lon="+lon, CurrentWeatherRecord.class);
	}
}
