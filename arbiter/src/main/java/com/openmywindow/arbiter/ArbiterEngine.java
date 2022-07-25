package com.openmywindow.arbiter;

import com.openmywindow.arbiter.domain.WindowRecommendation;
import com.openmywindow.arbiter.record.ForecastRecord;
import com.openmywindow.arbiter.record.HourlyForecastRecord;
import com.openmywindow.arbiter.util.ArbiterHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ArbiterEngine {

	private static final Logger log = LoggerFactory.getLogger(ArbiterEngine.class);

	private static final Double COLD_KELVIN = 291.483;
	private static final Double HOT_KELVIN = 297.039;
	private static final Double HIGH_HUMIDITY = 70.0;

	public WindowRecommendation determineComplexMessage(ForecastRecord forecastRecord) {
		WindowRecommendation windowRecommendation = new WindowRecommendation();

		populateCurrentWeatherInformation(windowRecommendation, forecastRecord);
		boolean openWindowByTemperature = processTemperatureDecision(windowRecommendation, forecastRecord);
		boolean openWindowByHumidity = processHumidityDecision(windowRecommendation, forecastRecord);
		if (openWindowByTemperature && openWindowByHumidity) {
			windowRecommendation.setStatus("Open");
			determineCloseWindowRecommendation(windowRecommendation, forecastRecord);
		}
		else {
			windowRecommendation.setStatus("Close");
			determineOpenWindowRecommendation(windowRecommendation, forecastRecord);
		}

		return windowRecommendation;
	}

	private void populateCurrentWeatherInformation(WindowRecommendation windowRecommendation, ForecastRecord forecastRecord) {
		windowRecommendation.setCurrentTemp(ArbiterHelper.convertKelvinToFahrenheit(forecastRecord.temp()));
		windowRecommendation.setDailyHighTemp(ArbiterHelper.convertKelvinToFahrenheit(forecastRecord.maxTemp()));
		windowRecommendation.setDailyLowTemp(ArbiterHelper.convertKelvinToFahrenheit(forecastRecord.minTemp()));
		windowRecommendation.setCurrentHumidity(forecastRecord.humidity());
	}

	private boolean processTemperatureDecision(WindowRecommendation windowRecommendation, ForecastRecord forecastRecord) {
		// too cold all day
		if (forecastRecord.maxTemp() < COLD_KELVIN) {
			windowRecommendation.setTempRecommendation("The high temp for today is too cold.");
			return false;
		}
		// too hot right now
		else if (forecastRecord.temp() > HOT_KELVIN) {
			windowRecommendation.setTempRecommendation("Too hot right now.");
			return false;
		}
		// good scenario to open your window
		else if (forecastRecord.temp() < HOT_KELVIN && forecastRecord.maxTemp() > HOT_KELVIN) {
			windowRecommendation.setTempRecommendation("Open your window.");
			return true;
		}

		windowRecommendation.setTempRecommendation("???? - No scenario found for your weather");
		return false;
	}

	private boolean processHumidityDecision(WindowRecommendation windowRecommendation, ForecastRecord forecastRecord) {

		if(forecastRecord.humidity() > HIGH_HUMIDITY) {
			windowRecommendation.setHumidityRecommendation("Too humid");
			return false;
		}

		windowRecommendation.setHumidityRecommendation("Humidity OK");
		return true;

	}

	private void determineCloseWindowRecommendation(WindowRecommendation windowRecommendation, ForecastRecord forecastRecord) {
		boolean foundATimeToClose = false;
		Long lastTimeEvaluated = null;
		for (HourlyForecastRecord hourlyForecast : forecastRecord.hourlyForecastList()) {
			log.info("hourly forecast: " + ArbiterHelper.convertToDateAndHour(hourlyForecast.dateTime()) + " : temp="+ArbiterHelper.printFahrenheitFromKelvin(hourlyForecast.temp()) + " : humidity=" + hourlyForecast.humidity());
			lastTimeEvaluated = hourlyForecast.dateTime();
			if (hourlyForecast.temp() > HOT_KELVIN || hourlyForecast.humidity() > HIGH_HUMIDITY) {
				foundATimeToClose = true;
				windowRecommendation.setNextRecommendation("Consider closing your window around " + ArbiterHelper.convertToDateAndHour(hourlyForecast.dateTime())
						+ " when the temp will be " + ArbiterHelper.convertKelvinToFahrenheit(hourlyForecast.temp())
						+ " and the humidity will be " + hourlyForecast.humidity() + ".");
				break;
			}
		}

		if(!foundATimeToClose && lastTimeEvaluated != null) {
			windowRecommendation.setNextRecommendation("Nothing in the forecast before " + ArbiterHelper.convertToDateAndHour(lastTimeEvaluated) + " shows a good time to close your window.");
		}

	}

	private void determineOpenWindowRecommendation(WindowRecommendation windowRecommendation, ForecastRecord forecastRecord) {
		boolean foundATimeToOpen = false;
		Long lastTimeEvaluated = null;
		for (HourlyForecastRecord hourlyForecast : forecastRecord.hourlyForecastList()) {
			log.info("hourly forecast: " + ArbiterHelper.convertToDateAndHour(hourlyForecast.dateTime()) + " : temp="+ArbiterHelper.printFahrenheitFromKelvin(hourlyForecast.temp()) + " : humidity=" + hourlyForecast.humidity());
			lastTimeEvaluated = hourlyForecast.dateTime();
			if (hourlyForecast.temp() < HOT_KELVIN && hourlyForecast.humidity() <= HIGH_HUMIDITY) {
				foundATimeToOpen = true;
				windowRecommendation.setNextRecommendation("Consider opening your window around " + ArbiterHelper.convertToDateAndHour(hourlyForecast.dateTime())
						+ " when the temp will be " + ArbiterHelper.convertKelvinToFahrenheit(hourlyForecast.temp())
						+ " and the humidity will be " + hourlyForecast.humidity() + ".");
				break;
			}
		}

		if(!foundATimeToOpen && lastTimeEvaluated != null) {
			windowRecommendation.setNextRecommendation("Nothing in the forecast before " + ArbiterHelper.convertToDateAndHour(lastTimeEvaluated) + " shows a good time to open your window.");
		}
	}

}
