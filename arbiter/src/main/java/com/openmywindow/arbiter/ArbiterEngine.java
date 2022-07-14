package com.openmywindow.arbiter;

import com.openmywindow.arbiter.record.ForecastRecord;
import com.openmywindow.arbiter.record.WindowRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ArbiterEngine {

	private static final Logger log = LoggerFactory.getLogger(ArbiterEngine.class);

	public WindowRecord determineWindowStatus(ForecastRecord dailyWeather) {

		String windowStatus = "";

		if (dailyWeather.maxTemp() < 65) {
			windowStatus = "Brr.... Keep the window closed today.  It is currently " + dailyWeather.temp() + " and the high will only be " + dailyWeather.maxTemp() +
				" with a low of " + dailyWeather.minTemp();
		}
		else if (dailyWeather.minTemp() > 75) {
			windowStatus = "Keep the windows closed.  Use the AC today because the temp is " + dailyWeather.temp() + " and it isn't getting lower than " +
					dailyWeather.minTemp() + ".  But it's going to be a high of " + dailyWeather.maxTemp();
		}
		else if (dailyWeather.temp() > 75) {
			windowStatus = "It's hot outside.  Close the windows because the temp is " + dailyWeather.temp() + " with a high of " +
					dailyWeather.maxTemp() + ".  The low will be " + dailyWeather.minTemp() + ".";
		}
		else if (dailyWeather.temp() > 65 && dailyWeather.temp() < 75) {
			windowStatus = "Open up the windows!  It is a great temperature outside at " + dailyWeather.temp() +".  The low is " + dailyWeather.minTemp() +
					" with a high of " + dailyWeather.maxTemp() +".";
		}
		else if (dailyWeather.temp() < 65 && dailyWeather.maxTemp() > 70) {
			windowStatus = "Free air conditioning?  It is " + dailyWeather.temp() + " outside now but it will heat up to " + dailyWeather.maxTemp() +
					".  The low today is " + dailyWeather.minTemp() + ".  Get the free cooling inside while you can.";
		}
		else {
			windowStatus = "Not sure what to do with a temp of " + dailyWeather.temp() + " with a low of " + dailyWeather.minTemp() +
					" and a high of " + dailyWeather.maxTemp();
		}

		return new WindowRecord(windowStatus);

	}
}
