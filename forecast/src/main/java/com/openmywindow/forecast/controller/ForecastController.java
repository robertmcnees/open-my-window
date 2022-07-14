package com.openmywindow.forecast.controller;

import com.openmywindow.forecast.record.ForecastResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.openmywindow.forecast.service.ForecastService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/current")
public class ForecastController {

	private static final Logger log = LoggerFactory.getLogger(ForecastController.class);

	private final ForecastService forecastService;

	public ForecastController(ForecastService forecastService) {
		this.forecastService = forecastService;
	}

	@GetMapping("currentWeather")
	public ForecastResponse getCurrentWeather(@RequestParam(name = "lat") Double lat, @RequestParam(name = "lon") Double lon) {
		return forecastService.getOpenWeatherApiCurrentWeather(lat, lon);
	}

}
