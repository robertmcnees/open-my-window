package com.openmywindow.arbiter.service;

import com.openmywindow.arbiter.record.DailyWeatherRecord;

public interface WeatherService {

	DailyWeatherRecord getDailyWeather(String postalCode);
}
