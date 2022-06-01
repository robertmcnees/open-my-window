package com.openmywindow.arbiter.controller;

import com.openmywindow.arbiter.ArbiterEngine;
import com.openmywindow.arbiter.domain.DailyWeather;
import com.openmywindow.arbiter.domain.Window;
import com.openmywindow.arbiter.service.WeatherService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/arbiter")
public class ArbiterController {

	private final ArbiterEngine engine = new ArbiterEngine();
	private final WeatherService weatherService;

	public ArbiterController(WeatherService weatherService) {
		this.weatherService = weatherService;
	}

	@GetMapping("test")
	public String getTest() {
		return "hello window";
	}

	@GetMapping("sampleWindow")
	public Window getSample() {
		return new Window("sampleStatus");
	}

	@GetMapping("myWindow")
	public Window makeCall(@RequestParam(name = "zipcode", defaultValue = "15108") String zipcode) {
		DailyWeather dailyWeather = weatherService.getDailyWeather(zipcode);
		return engine.determineWindowStatus(dailyWeather);
	}
}
