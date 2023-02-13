package com.openmywindow.arbiter;

import com.openmywindow.arbiter.domain.CurrentWeather;
import com.openmywindow.arbiter.domain.WindowChange;
import com.openmywindow.arbiter.domain.WindowStatus;
import com.openmywindow.arbiter.record.ForecastRecord;
import com.openmywindow.arbiter.record.HourlyForecastRecord;
import com.openmywindow.arbiter.util.ArbiterHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ArbiterEngine {

	private static final Logger log = LoggerFactory.getLogger(ArbiterEngine.class);

	/***
	 * All temperature calculations and return values are in Kelvin
	 * @param forecastRecord
	 * @param comfortableTemperature
	 * @return
	 */
	public WindowStatus determineWindowStatus(ForecastRecord forecastRecord, Double comfortableTemperature) {
		CurrentWeather currentWeather = new CurrentWeather(forecastRecord.temp(), forecastRecord.maxTemp(), forecastRecord.minTemp());

		WindowStatusEnum windowStatusEnum = determineWindowOpenCloseStatus(forecastRecord, comfortableTemperature);
		WindowChange nextWindowChange = determineNextWindowChange(windowStatusEnum, forecastRecord, comfortableTemperature);

		return new WindowStatus((windowStatusEnum == WindowStatusEnum.OPEN) ? "Open" : "Close", currentWeather, nextWindowChange);
	}

	private WindowStatusEnum determineWindowOpenCloseStatus(ForecastRecord forecastRecord, Double comfortableTemperature) {

		// too cold all day
		if (forecastRecord.maxTemp() < comfortableTemperature) {
			log.info("Too cold right now");
			return WindowStatusEnum.CLOSED_COLD;
		}

		// too hot right now
		if (forecastRecord.temp() > comfortableTemperature) {
			log.info("Too hot right now");
			return WindowStatusEnum.CLOSED_HOT;
		}

		// good scenario to open your window
		if (forecastRecord.temp() < comfortableTemperature && forecastRecord.maxTemp() > comfortableTemperature) {
			log.info("Good scenario to open your window.");
			return WindowStatusEnum.OPEN;
		}

		log.info("No scenario defined");
		return null;
	}

	private WindowChange determineNextWindowChange(WindowStatusEnum windowStatusEnum, ForecastRecord forecastRecord, Double comfortableTemperature) {

		for (HourlyForecastRecord hourlyForecast : forecastRecord.hourlyForecastList()) {
			log.info("hourly forecast: " + ArbiterHelper.convertToDateAndHour(hourlyForecast.dateTime()) + " : temp=" + ArbiterHelper.printFahrenheitFromKelvin(hourlyForecast.temp()) + " : humidity=" + hourlyForecast.humidity());

			// Window is open and we found a time that it will get too hot
			if (windowStatusEnum == WindowStatusEnum.OPEN && hourlyForecast.temp() > comfortableTemperature) {
				return new WindowChange("Close", hourlyForecast.dateTime(), hourlyForecast.temp());
			}

			// Window is open and we found a time that it will get too cold
			if (windowStatusEnum == WindowStatusEnum.OPEN && hourlyForecast.temp() < comfortableTemperature - 7) {
				return new WindowChange("Close", hourlyForecast.dateTime(), hourlyForecast.temp());
			}

			// Window is closed because it is too cold outside and we found a time it will warm up
			if (windowStatusEnum == WindowStatusEnum.CLOSED_COLD && hourlyForecast.temp() > comfortableTemperature - 7) {
				return new WindowChange("Open", hourlyForecast.dateTime(), hourlyForecast.temp());
			}

			// Window is closed because it is too hot outside and we found a time it will cool down
			if (windowStatusEnum == WindowStatusEnum.CLOSED_HOT && hourlyForecast.temp() < comfortableTemperature) {
				return new WindowChange("Open", hourlyForecast.dateTime(), hourlyForecast.temp());
			}

		}
		return null;
	}

}
