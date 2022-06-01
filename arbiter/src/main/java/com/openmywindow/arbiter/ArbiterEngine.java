package com.openmywindow.arbiter;

import com.openmywindow.arbiter.domain.DailyWeather;
import com.openmywindow.arbiter.domain.Window;

public class ArbiterEngine {

	public Window determineWindowStatus(DailyWeather dailyWeather) {
		System.out.println("Current temp = " + dailyWeather.getCurrentTemp());
		System.out.println("High temp = " + dailyWeather.getHighTemp());
		System.out.println("Low temp = " + dailyWeather.getLowTemp());

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
