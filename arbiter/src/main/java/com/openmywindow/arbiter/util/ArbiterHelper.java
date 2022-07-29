package com.openmywindow.arbiter.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.openmywindow.arbiter.domain.TemperatureScale;

public class ArbiterHelper {

	private static final SimpleDateFormat dateOnlySimpleDateFormat = new SimpleDateFormat("yyyyMMdd hh a");

	public static String printFahrenheitFromKelvin(Double temp) {
		Double fahrenheitTemp = convertKelvinToFahrenheit(temp);
		return Integer.toString(fahrenheitTemp.intValue());
	}

	public static String convertToDateAndHour(Long dateTime) {
		return dateOnlySimpleDateFormat.format(new Date(dateTime * 1000L));
	}

	public static Double convertKelvinTemperature(Double temp, TemperatureScale toScale) {
		return convertTemperature(temp, TemperatureScale.K, toScale);
	}

	public static Double convertTemperature(Double temp, TemperatureScale fromScale, TemperatureScale toScale) {
		if (fromScale == toScale) return temp;

		if (fromScale == TemperatureScale.K) {
			if (toScale == TemperatureScale.F) {
				return convertKelvinToFahrenheit(temp);
			}
			else if (toScale == TemperatureScale.C) {
				return convertKelvinToCelsius(temp);
			}
		}

		if (fromScale == TemperatureScale.C) {
			if (toScale == TemperatureScale.F) {
				return convertCelsiusToFahrenheit(temp);
			}
			else if (toScale == TemperatureScale.K) {
				return convertCelsiusToKelvin(temp);
			}
		}

		if (fromScale == TemperatureScale.F) {
			if (toScale == TemperatureScale.C) {
				return convertFahrenheitToCelsius(temp);
			}
			else if (toScale == TemperatureScale.K) {
				return convertFahrenheitToKelvin(temp);
			}
		}

		return -1.0;
	}

	private static Double convertFahrenheitToCelsius(Double temp) {
		return (temp - 32) * 5/9;
	}

	private static Double convertCelsiusToFahrenheit(Double temp) {
		return (temp * 9/5) + 32;
	}

	private static Double convertKelvinToFahrenheit(Double temp) {
		return (temp - 273.15) * 9 / 5 + 32;
	}

	private static Double convertKelvinToCelsius(Double temp) {
		return temp - 273.15;
	}

	private static Double convertCelsiusToKelvin(Double temp) {
		return temp + 273.15;
	}

	private static Double convertFahrenheitToKelvin(Double temp) {
		return (temp - 32) * 5 / 9 + 273.15;
	}

}
