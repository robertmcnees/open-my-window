package com.openmywindow.arbiter.controller;

import com.openmywindow.arbiter.ArbiterEngine;
import com.openmywindow.arbiter.record.ForecastRecord;
import com.openmywindow.arbiter.record.GeocodeCoordinates;
import com.openmywindow.arbiter.record.WindowRecord;
import com.openmywindow.arbiter.service.ForecastService;
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
	private final ForecastService forecastService;

	public ArbiterController(GeocodeService geocodeService, ForecastService forecastService) {
		this.geocodeService = geocodeService;
		this.forecastService = forecastService;
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
		ForecastRecord forecast = forecastService.getCurrentWeather(coordinates.lat(), coordinates.lon());
		return engine.determineWindowStatus(new ForecastRecord(forecast.temp(), forecast.minTemp(), forecast.maxTemp()));
	}
}
