package com.openmywindow.arbiter;

import com.openmywindow.arbiter.domain.TemperatureScale;
import com.openmywindow.arbiter.domain.WindowRecommendation;
import com.openmywindow.arbiter.record.ForecastRecord;
import com.openmywindow.arbiter.record.HourlyForecastRecord;
import com.openmywindow.arbiter.util.ArbiterHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ArbiterEngine {

	private static final Logger log = LoggerFactory.getLogger(ArbiterEngine.class);

	private static final Double COLD_TEMP_OFFSET_KELVIN = 7.0;

	public WindowRecommendation determineComplexMessage(ForecastRecord forecastRecord, Double comfortableTemperature,
			Double comfortableHumidity, TemperatureScale displayTemperatureScale) {

		WindowRecommendation windowRecommendation = new WindowRecommendation();

		populateCurrentWeatherInformation(windowRecommendation, forecastRecord, displayTemperatureScale);
		boolean openWindowByTemperature = processTemperatureDecision(windowRecommendation, forecastRecord, comfortableTemperature);
		boolean openWindowByHumidity = processHumidityDecision(windowRecommendation, forecastRecord, comfortableHumidity);
		if (openWindowByTemperature && openWindowByHumidity) {
			windowRecommendation.setStatus("Open");
			determineCloseWindowRecommendation(windowRecommendation, forecastRecord, comfortableTemperature, comfortableHumidity, displayTemperatureScale);
		}
		else {
			windowRecommendation.setStatus("Close");
			determineOpenWindowRecommendation(windowRecommendation, forecastRecord, comfortableTemperature, comfortableHumidity, displayTemperatureScale);
		}

		return windowRecommendation;
	}

	private void populateCurrentWeatherInformation(WindowRecommendation windowRecommendation, ForecastRecord forecastRecord, TemperatureScale displayTemperatureScale) {
		windowRecommendation.setCurrentTemp(ArbiterHelper.convertKelvinTemperature(forecastRecord.temp(), displayTemperatureScale));
		windowRecommendation.setDailyHighTemp(ArbiterHelper.convertKelvinTemperature(forecastRecord.maxTemp(), displayTemperatureScale));
		windowRecommendation.setDailyLowTemp(ArbiterHelper.convertKelvinTemperature(forecastRecord.minTemp(), displayTemperatureScale));
		windowRecommendation.setCurrentHumidity(forecastRecord.humidity());
	}

	private boolean processTemperatureDecision(WindowRecommendation windowRecommendation, ForecastRecord forecastRecord, Double comfortableTemperature) {
		// too cold all day
		if (forecastRecord.maxTemp() < comfortableTemperature - COLD_TEMP_OFFSET_KELVIN) {
			windowRecommendation.setTempRecommendation("The high temp for today is too cold.");
			return false;
		}
		// too hot right now
		else if (forecastRecord.temp() > comfortableTemperature) {
			windowRecommendation.setTempRecommendation("Too hot right now.");
			return false;
		}
		// good scenario to open your window
		else if (forecastRecord.temp() < comfortableTemperature && forecastRecord.maxTemp() > comfortableTemperature) {
			windowRecommendation.setTempRecommendation("Open your window.");
			return true;
		}

		windowRecommendation.setTempRecommendation("???? - No scenario found for your weather");
		return false;
	}

	private boolean processHumidityDecision(WindowRecommendation windowRecommendation, ForecastRecord forecastRecord, Double comfortableHumidity) {

		if(forecastRecord.humidity() > comfortableHumidity) {
			windowRecommendation.setHumidityRecommendation("Too humid");
			return false;
		}

		windowRecommendation.setHumidityRecommendation("Humidity OK");
		return true;

	}

	private void determineCloseWindowRecommendation(WindowRecommendation windowRecommendation, ForecastRecord forecastRecord,
			Double comfortableTemperature, Double comfortableHumidity, TemperatureScale displayTemperatureScale) {
		boolean foundATimeToClose = false;
		Long lastTimeEvaluated = null;
		for (HourlyForecastRecord hourlyForecast : forecastRecord.hourlyForecastList()) {
			log.info("hourly forecast: " + ArbiterHelper.convertToDateAndHour(hourlyForecast.dateTime()) + " : temp=" + ArbiterHelper.printFahrenheitFromKelvin(hourlyForecast.temp()) + " : humidity=" + hourlyForecast.humidity());
			lastTimeEvaluated = hourlyForecast.dateTime();
			if (hourlyForecast.temp() > comfortableTemperature || hourlyForecast.humidity() > comfortableHumidity) {
				foundATimeToClose = true;
				windowRecommendation.setNextRecommendation("Consider closing your window around " + ArbiterHelper.convertToDateAndHour(hourlyForecast.dateTime())
						+ " when the temp will be " + ArbiterHelper.convertKelvinTemperature(hourlyForecast.temp(), displayTemperatureScale)
						+ " and the humidity will be " + hourlyForecast.humidity() + ".");
				break;
			}
		}

		if(!foundATimeToClose && lastTimeEvaluated != null) {
			windowRecommendation.setNextRecommendation("Nothing in the forecast before " + ArbiterHelper.convertToDateAndHour(lastTimeEvaluated) + " shows a good time to close your window.");
		}

	}

	private void determineOpenWindowRecommendation(WindowRecommendation windowRecommendation, ForecastRecord forecastRecord,
			Double comfortableTemperature, Double comfortableHumidity, TemperatureScale displayTemperatureScale) {
		boolean foundATimeToOpen = false;
		Long lastTimeEvaluated = null;
		for (HourlyForecastRecord hourlyForecast : forecastRecord.hourlyForecastList()) {
			log.info("hourly forecast: " + ArbiterHelper.convertToDateAndHour(hourlyForecast.dateTime()) + " : temp=" + ArbiterHelper.printFahrenheitFromKelvin(hourlyForecast.temp()) + " : humidity=" + hourlyForecast.humidity());
			lastTimeEvaluated = hourlyForecast.dateTime();
			if (hourlyForecast.temp() < comfortableTemperature && hourlyForecast.humidity() <= comfortableHumidity) {
				foundATimeToOpen = true;
				windowRecommendation.setNextRecommendation("Consider opening your window around " + ArbiterHelper.convertToDateAndHour(hourlyForecast.dateTime())
						+ " when the temp will be " + ArbiterHelper.convertKelvinTemperature(hourlyForecast.temp(), displayTemperatureScale)
						+ " and the humidity will be " + hourlyForecast.humidity() + ".");
				break;
			}
		}

		if(!foundATimeToOpen && lastTimeEvaluated != null) {
			windowRecommendation.setNextRecommendation("Nothing in the forecast before " + ArbiterHelper.convertToDateAndHour(lastTimeEvaluated) + " shows a good time to open your window.");
		}
	}

}
