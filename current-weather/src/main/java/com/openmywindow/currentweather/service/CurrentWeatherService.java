package com.openmywindow.currentweather.service;

import com.openmywindow.currentweather.helper.Conversion;
import com.openmywindow.currentweather.record.CurrentWeatherResponse;
import com.openmywindow.currentweather.record.OpenWeatherApiCurrentWeatherResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

public class CurrentWeatherService {

	private static final Logger log = LoggerFactory.getLogger(CurrentWeatherService.class);
	private final RestTemplate restTemplate;

	@Value("${SECRET_OPENWEATHER_API_KEY:defaultValue}")
	private String OPEN_WEATHER_API_KEY;

	public CurrentWeatherService(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public CurrentWeatherResponse getOpenWeatherApiCurrentWeather(Double lat, Double lon) {

		OpenWeatherApiCurrentWeatherResponse response =
				restTemplate.getForObject(
						"https://api.openweathermap.org/data/2.5/weather?lat=" + lat + "&lon=" + lon + "&appid=" + OPEN_WEATHER_API_KEY,
						OpenWeatherApiCurrentWeatherResponse.class);

		return new CurrentWeatherResponse(
				Conversion.convertKelvinToFahrenheit(response.main().temp()),
				Conversion.convertKelvinToFahrenheit(response.main().temp_min()),
				Conversion.convertKelvinToFahrenheit(response.main().temp_max()));

	}

}
