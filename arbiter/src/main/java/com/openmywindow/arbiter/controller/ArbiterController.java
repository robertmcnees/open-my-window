package com.openmywindow.arbiter.controller;

import com.openmywindow.arbiter.ArbiterEngine;
import com.openmywindow.arbiter.domain.TemperatureScale;
import com.openmywindow.arbiter.domain.WindowRecommendation;
import com.openmywindow.arbiter.record.ForecastRecord;
import com.openmywindow.arbiter.record.GeocodeCoordinates;
import com.openmywindow.arbiter.service.ForecastService;
import com.openmywindow.arbiter.service.GeocodeService;
import com.openmywindow.arbiter.util.ArbiterHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Value;
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

	@Value("${COMFORTABLE_TEMPERATURE}")
	private String COMFORTABLE_TEMP;

	public ArbiterController(GeocodeService geocodeService, ForecastService forecastService) {
		this.geocodeService = geocodeService;
		this.forecastService = forecastService;
	}

	@GetMapping("test")
	public String controlTest() {
		return "hello window";
	}

	@GetMapping("coordinates")
	public String getCoordinates(@RequestParam String postalCode) {
		GeocodeCoordinates coordinates = geocodeService.getGeocodeCoordinates(postalCode, "US");
		return ("lat=" + coordinates.lat() + " : lon=" + coordinates.lon());
	}

	@GetMapping("forecast")
	public ForecastRecord getForecast(@RequestParam Double lat, @RequestParam Double lon) {
		return forecastService.getCurrentWeather(lat, lon);
	}

	@GetMapping("comfortableTemp")
	public String getComfortableTemp() {
		return COMFORTABLE_TEMP;
	}

	@GetMapping("window")
	public WindowRecommendation calculateWindowRecommendation(@RequestParam String postalCode,
			@RequestParam(defaultValue = "US", required = false) String countryCode,
			@RequestParam(defaultValue = "297.039", required = false) Double comfortableTemperature,
			@RequestParam(defaultValue = "100", required = false) Double comfortableHumidity,
			@RequestParam(defaultValue = "F", required = false) TemperatureScale units) {

		// if the user specified a temperature and a preferred unit then likely they specified the temp in the same unit
		if(comfortableTemperature != 297.039 && units != TemperatureScale.K) {
			if(units == TemperatureScale.C) {
				comfortableTemperature = ArbiterHelper.convertTemperature(comfortableTemperature, TemperatureScale.C, TemperatureScale.K);
			}
			else {
				comfortableTemperature = ArbiterHelper.convertTemperature(comfortableTemperature, TemperatureScale.F, TemperatureScale.K);
			}
		}

		GeocodeCoordinates coordinates = geocodeService.getGeocodeCoordinates(postalCode, countryCode);
		ForecastRecord forecast = forecastService.getCurrentWeather(coordinates.lat(), coordinates.lon());
		return engine.determineComplexMessage(forecast, comfortableTemperature, comfortableHumidity, units);
	}
}
