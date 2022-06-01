package com.openmywindow.arbiter;

import com.openmywindow.arbiter.domain.DailyWeather;
import com.openmywindow.arbiter.domain.Window;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ArbiterEngine {

	private static final Logger log = LoggerFactory.getLogger(ArbiterEngine.class);

	public Window determineWindowStatus(DailyWeather dailyWeather) {
		log.info("Current temp = " + dailyWeather.getCurrentTemp());
		log.info("High temp = " + dailyWeather.getHighTemp());
		log.info("Low temp = " + dailyWeather.getLowTemp());

		if (dailyWeather.getHighTemp() < 65) {
			return new Window("closed - too cold");
		}

		if (dailyWeather.getLowTemp() > 75) {
			return new Window("closed - too hot");
		}

		if (dailyWeather.getCurrentTemp() > 65 && dailyWeather.getCurrentTemp() < 75) {
			return new Window("open - good temp outside");
		}

		if (dailyWeather.getCurrentTemp() < 65 && dailyWeather.getHighTemp() > 70) {
			return new Window("open - cold now, hot later");
		}

		return new Window("closed - no scenario");
	}
}
