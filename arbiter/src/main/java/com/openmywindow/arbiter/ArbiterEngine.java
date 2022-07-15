package com.openmywindow.arbiter;

import com.openmywindow.arbiter.record.ForecastRecord;
import com.openmywindow.arbiter.record.HourlyForecastRecord;
import com.openmywindow.arbiter.record.WindowRecord;
import com.openmywindow.arbiter.util.ArbiterHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ArbiterEngine {

	private static final Logger log = LoggerFactory.getLogger(ArbiterEngine.class);

	private static final Double COLD_KELVIN = 291.483;
	private static final Double HOT_HELVIN = 297.039;


	public WindowRecord determineMessage(ForecastRecord dailyWeather) {

		String message = determineCurrentWindowRecommendation(dailyWeather) + " : " + determineNextOpenCloseRecommendation(dailyWeather);

		return new WindowRecord(message);

	}

	private String determineCurrentWindowRecommendation(ForecastRecord dailyWeather) {

		String windowStatus = "";

		if (dailyWeather.maxTemp() < COLD_KELVIN) {
			windowStatus = "Brr.... Keep the window closed today.  It is currently " + ArbiterHelper.convertKelvinToFahrenheit(dailyWeather.temp()) + " and the high will only be " + ArbiterHelper.convertKelvinToFahrenheit(dailyWeather.maxTemp()) +
					" with a low of " + ArbiterHelper.convertKelvinToFahrenheit(dailyWeather.minTemp());
		}
		else if (dailyWeather.minTemp() > HOT_HELVIN) {
			windowStatus = "Keep the windows closed.  Use the AC today because the temp is " + ArbiterHelper.convertKelvinToFahrenheit(dailyWeather.temp()) + " and it isn't getting lower than " +
					ArbiterHelper.convertKelvinToFahrenheit(dailyWeather.minTemp()) + ".  But it's going to be a high of " + ArbiterHelper.convertKelvinToFahrenheit(dailyWeather.maxTemp());
		}
		else if (dailyWeather.temp() > HOT_HELVIN) {
			windowStatus = "It's hot outside.  Close the windows because the temp is " + ArbiterHelper.printFahrenheitFromKelvin(dailyWeather.temp()) + " with a high of " +
					ArbiterHelper.printFahrenheitFromKelvin(dailyWeather.maxTemp()) + ".  The low will be " + ArbiterHelper.printFahrenheitFromKelvin(dailyWeather.minTemp()) + ".";
		}
		else if (dailyWeather.temp() > COLD_KELVIN && dailyWeather.temp() < HOT_HELVIN) {
			windowStatus = "Open up the windows!  It is a great temperature outside at " + ArbiterHelper.convertKelvinToFahrenheit(dailyWeather.temp()) + ".  The low is " + ArbiterHelper.convertKelvinToFahrenheit(dailyWeather.minTemp()) +
					" with a high of " + ArbiterHelper.convertKelvinToFahrenheit(dailyWeather.maxTemp()) + ".";
		}
		else if (dailyWeather.temp() < COLD_KELVIN && dailyWeather.maxTemp() > HOT_HELVIN) {
			windowStatus = "Free air conditioning?  It is " + ArbiterHelper.convertKelvinToFahrenheit(dailyWeather.temp()) + " outside now but it will heat up to " + ArbiterHelper.convertKelvinToFahrenheit(dailyWeather.maxTemp()) +
					".  The low today is " + ArbiterHelper.convertKelvinToFahrenheit(dailyWeather.minTemp()) + ".  Get the free cooling inside while you can.";
		}
		else {
			windowStatus = "Not sure what to do with a temp of " + dailyWeather.temp() + " with a low of " + dailyWeather.minTemp() +
					" and a high of " + dailyWeather.maxTemp();
		}

		return windowStatus;
	}

	private String determineNextOpenCloseRecommendation(ForecastRecord dailyWeather) {
		String openClose = "";


		if (dailyWeather.temp() > HOT_HELVIN) {
			for (HourlyForecastRecord hourlyForecast : dailyWeather.hourlyForecastList()) {
				if (hourlyForecast.temp() < HOT_HELVIN) {
					openClose = "Consider opening your window around " + ArbiterHelper.convertToDateAndHour(hourlyForecast.dateTime()) + ".";
					break;
				}
			}
		}
		else if (dailyWeather.temp() < HOT_HELVIN) {
			for (HourlyForecastRecord hourlyForecast : dailyWeather.hourlyForecastList()) {
				if (hourlyForecast.temp() > HOT_HELVIN) {
					openClose = "Consider closing your window around " + ArbiterHelper.convertToDateAndHour(hourlyForecast.dateTime()) + ".";
					break;
				}
			}
		}

		return openClose;
	}

}
