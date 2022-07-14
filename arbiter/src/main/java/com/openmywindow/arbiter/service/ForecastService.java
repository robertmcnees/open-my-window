package com.openmywindow.arbiter.service;

import com.openmywindow.arbiter.record.ForecastRecord;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

public class ForecastService {

	private final RestTemplate restTemplate;

	@Value("${forecastserviceurl:omw-currentweather}")
	private String currentWeatherServiceUrl;

	public ForecastService(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public ForecastRecord getCurrentWeather(Double lat, Double lon) {
		return restTemplate.getForObject("http://" + currentWeatherServiceUrl + "/current/currentWeather?lat="+lat+"&lon="+lon, ForecastRecord.class);
	}
}
