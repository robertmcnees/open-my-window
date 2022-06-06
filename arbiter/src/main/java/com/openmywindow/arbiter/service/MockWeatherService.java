package com.openmywindow.arbiter.service;

import java.util.Random;

import com.openmywindow.arbiter.record.DailyWeatherRecord;

public class MockWeatherService implements WeatherService {

	private static final Integer MAX_HIGH = 100;
	private static final Integer MIN_HIGH = 50;
	private static final Integer MAX_LOW = 15;
	private static final Integer MIN_LOW = 0;
	private final Random random = new Random();

	@Override
	public DailyWeatherRecord getDailyWeather(String zipCode) {
		Integer highTemp = random.nextInt(MAX_HIGH - MIN_HIGH) + MIN_HIGH;
		Integer lowTemp = random.nextInt(MAX_LOW - MIN_LOW) + MIN_LOW;

		Integer currentTemp = random.nextInt(highTemp - lowTemp) + lowTemp;


		return new DailyWeatherRecord(currentTemp, highTemp, lowTemp);
	}
}
