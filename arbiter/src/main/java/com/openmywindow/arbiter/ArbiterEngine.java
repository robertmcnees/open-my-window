package com.openmywindow.arbiter;

import com.openmywindow.arbiter.record.DailyWeatherRecord;
import com.openmywindow.arbiter.record.WindowRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ArbiterEngine {

	private static final Logger log = LoggerFactory.getLogger(ArbiterEngine.class);

	public WindowRecord determineWindowStatus(DailyWeatherRecord dailyWeather) {
		log.info("Current temp = " + dailyWeather.currentTemp());
		log.info("High temp = " + dailyWeather.highTemp());
		log.info("Low temp = " + dailyWeather.lowTemp());

		if (dailyWeather.highTemp() < 65) {
			return new WindowRecord("closed - too cold");
		}

		if (dailyWeather.lowTemp() > 75) {
			return new WindowRecord("closed - too hot");
		}

		if (dailyWeather.currentTemp() > 65 && dailyWeather.currentTemp() < 75) {
			return new WindowRecord("open - good temp outside");
		}

		if (dailyWeather.currentTemp() < 65 && dailyWeather.highTemp() > 70) {
			return new WindowRecord("open - cold now, hot later");
		}

		return new WindowRecord("closed - no scenario");
	}
}
