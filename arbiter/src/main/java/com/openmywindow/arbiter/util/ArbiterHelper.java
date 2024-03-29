package com.openmywindow.arbiter.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.openmywindow.arbiter.domain.TemperatureScale;

public class ArbiterHelper {

	private static final SimpleDateFormat dateOnlySimpleDateFormat = new SimpleDateFormat("yyyyMMdd hh a");

	private static DecimalFormat decimalFormat = new DecimalFormat("#.#");

	public static String printFromKelvin(Double temp, TemperatureScale scale) {
		switch (scale) {
		case F: return printFahrenheitFromKelvin(temp);
		case C: return printCelsiusFromKelvin(temp);
		}
		return Double.toString(temp);
	}

	public static String printFahrenheitFromKelvin(Double temp) {
		Double fahrenheitTemp = convertKelvinToFahrenheit(temp);
		return Integer.toString(fahrenheitTemp.intValue());
	}

	public static String printCelsiusFromKelvin(Double temp) {
		Double fahrenheitTemp = convertKelvinToCelsius(temp);
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
			else {
				return roundNumber(temp);
			}
		}

		if (fromScale == TemperatureScale.C) {
			if (toScale == TemperatureScale.F) {
				return convertCelsiusToFahrenheit(temp);
			}
			else if (toScale == TemperatureScale.K) {
				return convertCelsiusToKelvin(temp);
			}
			else {
				return roundNumber(temp);
			}
		}

		if (fromScale == TemperatureScale.F) {
			if (toScale == TemperatureScale.C) {
				return convertFahrenheitToCelsius(temp);
			}
			else if (toScale == TemperatureScale.K) {
				return convertFahrenheitToKelvin(temp);
			}
			else {
				return roundNumber(temp);
			}
		}

		return -1.0;
	}

	private static Double convertFahrenheitToCelsius(Double temp) {
		return roundNumber((temp - 32) * 5/9);
	}

	private static Double convertCelsiusToFahrenheit(Double temp) {
		return roundNumber((temp * 9/5) + 32);
	}

	private static Double convertKelvinToFahrenheit(Double temp) {
		return roundNumber((temp - 273.15) * 9 / 5 + 32);
	}

	private static Double convertKelvinToCelsius(Double temp) {
		return roundNumber(temp - 273.15);
	}

	private static Double convertCelsiusToKelvin(Double temp) {
		return roundNumber(temp + 273.15);
	}

	private static Double convertFahrenheitToKelvin(Double temp) {
		return roundNumber((temp - 32) * 5 / 9 + 273.15);
	}

	private static Double roundNumber(Double number) {
		return new BigDecimal(number).setScale(1, RoundingMode.FLOOR).doubleValue();
	}
}
