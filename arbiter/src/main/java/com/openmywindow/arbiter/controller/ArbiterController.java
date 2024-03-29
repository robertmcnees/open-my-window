package com.openmywindow.arbiter.controller;

import com.openmywindow.arbiter.ArbiterEngine;
import com.openmywindow.arbiter.domain.TemperatureScale;
import com.openmywindow.arbiter.domain.WindowStatus;
import com.openmywindow.arbiter.record.ForecastRecord;
import com.openmywindow.arbiter.record.GeocodeCoordinates;
import com.openmywindow.arbiter.service.ForecastService;
import com.openmywindow.arbiter.service.GeocodeService;
import com.openmywindow.arbiter.util.ArbiterHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/window")
public class ArbiterController {

	private static final Logger log = LoggerFactory.getLogger(ArbiterController.class);
	private final ArbiterEngine engine = new ArbiterEngine();
	private final GeocodeService geocodeService;
	private final ForecastService forecastService;

	@Value("${COMFORTABLE_TEMPERATURE:298}")
	private String COMFORTABLE_TEMP;


	public ArbiterController(GeocodeService geocodeService, ForecastService forecastService) {
		this.geocodeService = geocodeService;
		this.forecastService = forecastService;
	}

	@GetMapping("{countryCode}/{postalCode}")
	public WindowStatus calculateUnitedStatesWindowRecommendation(@PathVariable String postalCode, @PathVariable String countryCode, @RequestParam(defaultValue = "F", required = false) TemperatureScale units) {

		GeocodeCoordinates coordinates = geocodeService.getGeocodeCoordinates(postalCode, countryCode);
		ForecastRecord forecast = forecastService.getCurrentWeather(coordinates.lat(), coordinates.lon());
		WindowStatus windowStatus = engine.determineWindowStatus(forecast, Double.parseDouble(COMFORTABLE_TEMP));

		return processTemperatureConversions(windowStatus, units);
	}

	private WindowStatus processTemperatureConversions(WindowStatus windowStatus, TemperatureScale units) {
		windowStatus.getCurrentWeather().setCurrentTemp(
				ArbiterHelper.convertTemperature(windowStatus.getCurrentWeather().getCurrentTemp(),TemperatureScale.K, units));
		windowStatus.getCurrentWeather().setDailyHighTemp(
				ArbiterHelper.convertTemperature(windowStatus.getCurrentWeather().getDailyHighTemp(),TemperatureScale.K, units));
		windowStatus.getCurrentWeather().setDailyLowTemp(
				ArbiterHelper.convertTemperature(windowStatus.getCurrentWeather().getDailyLowTemp(),TemperatureScale.K, units));

		if(windowStatus.getWindowChange() != null) {
			windowStatus.getWindowChange().setTemp(
					ArbiterHelper.convertTemperature(windowStatus.getWindowChange().getTemp(), TemperatureScale.K, units));
		}

		return windowStatus;
	}
	// ************************************
	// Test Endpoints
	// ************************************

	@GetMapping("test")
	public String controlTest() {
		return "hello window";
	}

	@GetMapping("coordinates/{countryCode}/{postalCode}")
	public String getCoordinates(@PathVariable String postalCode, @PathVariable String countryCode) {
		GeocodeCoordinates coordinates = geocodeService.getGeocodeCoordinates(postalCode, countryCode);
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

}
