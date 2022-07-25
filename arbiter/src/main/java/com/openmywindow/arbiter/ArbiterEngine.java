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
	private static final Double HOT_HELVIN = 297.039;


	public WindowRecommendation determineComplexMessage(ForecastRecord forecastRecord) {
		WindowRecommendation windowRecommendation = new WindowRecommendation();

		populateCurrentWeatherInformation(windowRecommendation, forecastRecord);
		boolean openWindowByTemperature = processTemperatureDecision(windowRecommendation, forecastRecord);
		if (openWindowByTemperature) {
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
	}

	private boolean processTemperatureDecision(WindowRecommendation windowRecommendation, ForecastRecord forecastRecord) {
		// too cold all day
		if (forecastRecord.maxTemp() < COLD_KELVIN) {
			windowRecommendation.setTempRecommendation("The high temp for today is too cold.");
			return false;
		}
		// too hot right now
		else if (forecastRecord.temp() > HOT_HELVIN) {
			windowRecommendation.setTempRecommendation("Too hot right now.");
			return false;
		}
		// good scenario to open your window
		else if (forecastRecord.temp() < HOT_HELVIN && forecastRecord.maxTemp() > HOT_HELVIN) {
			windowRecommendation.setTempRecommendation("Open your window.");
			return true;
		}

		windowRecommendation.setTempRecommendation("???? - No scenario found for your weather");
		return false;
	}

	private void determineCloseWindowRecommendation(WindowRecommendation windowRecommendation, ForecastRecord forecastRecord) {
		for (HourlyForecastRecord hourlyForecast : forecastRecord.hourlyForecastList()) {
			if (hourlyForecast.temp() > HOT_HELVIN) {
				windowRecommendation.setNextRecommendation("Consider closing your window around " + ArbiterHelper.convertToDateAndHour(hourlyForecast.dateTime()) + " when the temp will be " + ArbiterHelper.convertKelvinToFahrenheit(hourlyForecast.temp()) + ".");
				break;
			}
		}
	}

	private void determineOpenWindowRecommendation(WindowRecommendation windowRecommendation, ForecastRecord forecastRecord) {
		for (HourlyForecastRecord hourlyForecast : forecastRecord.hourlyForecastList()) {
			if (hourlyForecast.temp() < HOT_HELVIN) {
				windowRecommendation.setNextRecommendation("Consider opening your window around " + ArbiterHelper.convertToDateAndHour(hourlyForecast.dateTime()) + " when the temp will be " + ArbiterHelper.convertKelvinToFahrenheit(hourlyForecast.temp()) + ".");
				break;
			}
		}
	}

}
