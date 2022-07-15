package com.openmywindow.forecast.service;

import java.util.Date;

import com.openmywindow.forecast.helper.Conversion;
import com.openmywindow.forecast.record.ForecastResponse;
import com.openmywindow.forecast.record.openweather.OneCallResponse;
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

		OneCallResponse response =
				restTemplate.getForObject(
						"https://api.openweathermap.org/data/2.5/onecall?lat=" + lat + "&lon=" + lon
								//+ "&exclude=minutely,daily,alerts" //include hourly, current
								+ "&exclude=minutely,alerts,hourly" //include current, daily
								+ "&appid=" + OPEN_WEATHER_API_KEY,
						OneCallResponse.class);


		Double highTemp = Conversion.convertKelvinToFahrenheit(response.current().temp());
		Double lowTemp = Conversion.convertKelvinToFahrenheit(response.current().temp());

		if (response.daily() != null && response.daily().size() > 0) {
			/// Assuming current date is at index 0.
			Date dailyForecastDate = new Date(response.daily().get(0).dt() * 1000L);
			lowTemp = response.daily().get(0).temp().min();
			highTemp = response.daily().get(0).temp().max();
		}

		return new ForecastResponse(
				Conversion.convertKelvinToFahrenheit(response.current().temp()),
				Conversion.convertKelvinToFahrenheit(lowTemp),
				Conversion.convertKelvinToFahrenheit(highTemp));

	}

}
