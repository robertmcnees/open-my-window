package com.openmywindow.forecast.service;

import com.openmywindow.forecast.helper.Conversion;
import com.openmywindow.forecast.record.ForecastResponse;
import com.openmywindow.forecast.record.OpenWeatherApiOneCallWeatherResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

public class ForecastService {

	private static final Logger log = LoggerFactory.getLogger(ForecastService.class);
	private final RestTemplate restTemplate;

	@Value("${SECRET_OPENWEATHER_API_KEY:defaultValue}")
	private String OPEN_WEATHER_API_KEY;

	public ForecastService(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public ForecastResponse getOpenWeatherApiCurrentWeather(Double lat, Double lon) {

		OpenWeatherApiOneCallWeatherResponse response =
				restTemplate.getForObject(
						"https://api.openweathermap.org/data/2.5/weather?lat=" + lat + "&lon=" + lon + "&appid=" + OPEN_WEATHER_API_KEY,
						OpenWeatherApiOneCallWeatherResponse.class);

		return new ForecastResponse(
				Conversion.convertKelvinToFahrenheit(response.main().temp()),
				Conversion.convertKelvinToFahrenheit(response.main().temp_min()),
				Conversion.convertKelvinToFahrenheit(response.main().temp_max()));

	}

}
