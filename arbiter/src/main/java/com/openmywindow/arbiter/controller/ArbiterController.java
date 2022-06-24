package com.openmywindow.arbiter.controller;

import com.openmywindow.arbiter.ArbiterEngine;
import com.openmywindow.arbiter.record.DailyWeatherRecord;
import com.openmywindow.arbiter.record.GeocodeCoordinates;
import com.openmywindow.arbiter.record.WindowRecord;
import com.openmywindow.arbiter.service.GeocodeService;
import com.openmywindow.arbiter.service.WeatherService;
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

	public ArbiterController(GeocodeService geocodeService) {
		this.geocodeService = geocodeService;
	}

	@GetMapping("test")
	public String getTest() {
		return "hello window";
	}

	@GetMapping("window")
	public WindowRecord makeCall(@RequestParam String postalCode) {
		GeocodeCoordinates coordinates = geocodeService.getGeocodeCoordinates(postalCode);
		log.info("coordinates - lat="+coordinates.lat() + "; lon=" + coordinates.lon());
		return engine.determineWindowStatus(new DailyWeatherRecord(0,100, 1000));
	}
}
