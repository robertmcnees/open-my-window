package com.openmywindow.arbiter.service;

import com.openmywindow.arbiter.record.ForecastRecord;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ForecastService {

	private final RestTemplate restTemplate;

	@Value("${forecastserviceurl:omw-currentweather}")
	private String currentWeatherServiceUrl;

	public ForecastService(RestTemplateBuilder restTemplateBuilder) {
		this.restTemplate = restTemplateBuilder.build();
	}

	public ForecastRecord getCurrentWeather(Double lat, Double lon) {
		return restTemplate.getForObject("http://" + currentWeatherServiceUrl + "/current/currentWeather?lat="+lat+"&lon="+lon, ForecastRecord.class);
	}
}
