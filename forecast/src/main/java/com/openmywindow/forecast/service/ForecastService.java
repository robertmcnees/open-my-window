package com.openmywindow.forecast.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.openmywindow.forecast.record.DailyForecast;
import com.openmywindow.forecast.record.ForecastResponse;
import com.openmywindow.forecast.record.HourlyForecast;
import com.openmywindow.forecast.record.openweather.OneCallResponse;
import com.openmywindow.forecast.record.openweather.daily.OpenWeatherDaily;
import com.openmywindow.forecast.record.openweather.hourly.OpenWeatherHourly;
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
								//+ "&exclude=minutely,alerts,hourly" //include current, daily
								+ "&exclude=minutely,alerts" //include current, daily, hourly
								+ "&appid=" + OPEN_WEATHER_API_KEY,
						OneCallResponse.class);


		Double highTemp = response.current().temp();
		Double lowTemp = response.current().temp();

		List<DailyForecast> dailyForecastList = new ArrayList<>();
		if (response.daily() != null && response.daily().size() > 0) {
			/// Assuming current date is at index 0.
			Date dailyForecastDate = new Date(response.daily().get(0).dt() * 1000L);
			lowTemp = response.daily().get(0).temp().min();
			highTemp = response.daily().get(0).temp().max();

			for (OpenWeatherDaily openWeatherDaily : response.daily()) {
				dailyForecastList.add(new DailyForecast(openWeatherDaily.dt(), openWeatherDaily.temp().min(), openWeatherDaily.temp()
						.max()));
			}
		}

		List<HourlyForecast> hourlyForecastList = new ArrayList<>();
		if (response.hourly() != null && response.hourly().size() > 0) {
			List<OpenWeatherHourly> openWeatherHourlyList = response.hourly();
			for (OpenWeatherHourly openWeatherHourly : openWeatherHourlyList) {
				hourlyForecastList.add(new HourlyForecast(openWeatherHourly.dt(), openWeatherHourly.temp(), openWeatherHourly.humidity()));
			}
		}


		return new ForecastResponse(
				response.current().temp(), lowTemp, highTemp, response.current().humidity(),
				dailyForecastList, hourlyForecastList);

	}

}
