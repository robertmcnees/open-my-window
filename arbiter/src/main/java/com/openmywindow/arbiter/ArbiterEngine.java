package com.openmywindow.arbiter;

import com.openmywindow.arbiter.record.CurrentWeatherRecord;
import com.openmywindow.arbiter.record.WindowRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ArbiterEngine {

	private static final Logger log = LoggerFactory.getLogger(ArbiterEngine.class);

	public WindowRecord determineWindowStatus(CurrentWeatherRecord dailyWeather) {
		log.info("Current temp = " + dailyWeather.temp());
		log.info("High temp = " + dailyWeather.maxTemp());
		log.info("Low temp = " + dailyWeather.minTemp());

		if (dailyWeather.maxTemp() < 65) {
			return new WindowRecord("closed - too cold");
		}

		if (dailyWeather.minTemp() > 75) {
			return new WindowRecord("closed - too hot");
		}

		if (dailyWeather.temp() > 65 && dailyWeather.temp() < 75) {
			return new WindowRecord("open - good temp outside");
		}

		if (dailyWeather.temp() < 65 && dailyWeather.maxTemp() > 70) {
			return new WindowRecord("open - cold now, hot later");
		}

		return new WindowRecord("closed - no scenario");
	}
}
