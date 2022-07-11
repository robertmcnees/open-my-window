package com.openmywindow.arbiter.controller;

import com.openmywindow.arbiter.ArbiterEngine;
import com.openmywindow.arbiter.record.CurrentWeatherRecord;
import com.openmywindow.arbiter.record.GeocodeCoordinates;
import com.openmywindow.arbiter.record.WindowRecord;
import com.openmywindow.arbiter.service.CurrentWeatherService;
import com.openmywindow.arbiter.service.GeocodeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/arbiter")
public class ArbiterController {

	private static final Logger log = LoggerFactory.getLogger(ArbiterController.class);
	private final ArbiterEngine engine = new ArbiterEngine();
	private final GeocodeService geocodeService;
	private final CurrentWeatherService currentWeatherService;

	public ArbiterController(GeocodeService geocodeService, CurrentWeatherService currentWeatherService) {
		this.geocodeService = geocodeService;
		this.currentWeatherService = currentWeatherService;
	}

	@GetMapping("test")
	public String getTest() {
		return "hello window";
	}

	@GetMapping("coordinates")
	public String getCoordinates(@RequestParam String postalCode) {
		GeocodeCoordinates coordinates = geocodeService.getGeocodeCoordinates(postalCode);
		return ("lat=" + coordinates.lat() + " : lon=" + coordinates.lon());
	}

	@GetMapping("window")
	public WindowRecord makeCall(@RequestParam String postalCode) {
		GeocodeCoordinates coordinates = geocodeService.getGeocodeCoordinates(postalCode);
		log.info("coordinates - lat=" + coordinates.lat() + "; lon=" + coordinates.lon());
		CurrentWeatherRecord currentWeather = currentWeatherService.getCurrentWeather(coordinates.lat(), coordinates.lon());
		return engine.determineWindowStatus(new CurrentWeatherRecord(currentWeather.temp(), currentWeather.minTemp(), currentWeather.maxTemp()));
	}
}
