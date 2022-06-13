package com.openmywindow.arbiter.controller;

import com.openmywindow.arbiter.ArbiterEngine;
import com.openmywindow.arbiter.record.DailyWeatherRecord;
import com.openmywindow.arbiter.record.WindowRecord;
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
	public WindowRecord getSample() {
		return new WindowRecord("sampleStatus");
	}

	@GetMapping("myWindow")
	public WindowRecord makeCall(@RequestParam(name = "postalCode", defaultValue = "15108") String postalCode) {
		DailyWeatherRecord dailyWeather = weatherService.getDailyWeather(postalCode);
		return engine.determineWindowStatus(dailyWeather);
	}
}
