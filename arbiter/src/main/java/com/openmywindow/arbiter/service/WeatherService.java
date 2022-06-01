package com.openmywindow.arbiter.service;

import com.openmywindow.arbiter.domain.DailyWeather;

public interface WeatherService {

	DailyWeather getDailyWeather(String zipCode);
}
