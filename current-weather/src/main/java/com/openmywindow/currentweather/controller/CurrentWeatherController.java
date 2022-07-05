package com.openmywindow.currentweather.controller;

import com.openmywindow.currentweather.record.CurrentWeatherResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.openmywindow.currentweather.service.CurrentWeatherService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/current")
public class CurrentWeatherController {

	private static final Logger log = LoggerFactory.getLogger(CurrentWeatherController.class);

	private final CurrentWeatherService currentWeatherService;

	public CurrentWeatherController(CurrentWeatherService currentWeatherService) {
		this.currentWeatherService = currentWeatherService;
	}

	@GetMapping("currentWeather")
	public CurrentWeatherResponse getCurrentWeather(@RequestParam(name = "lat") Double lat, @RequestParam(name = "lon") Double lon) {
		return currentWeatherService.getOpenWeatherApiCurrentWeather(lat, lon);
	}

}
