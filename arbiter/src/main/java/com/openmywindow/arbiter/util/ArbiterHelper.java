package com.openmywindow.arbiter.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ArbiterHelper {

	private static final SimpleDateFormat dateOnlySimpleDateFormat = new SimpleDateFormat("yyyyMMdd hh a");



	public static Double convertKelvinToFahrenheit(Double temp) {
		return (temp - 273.15) * 9 / 5 + 32;
	}

	public static String printFahrenheitFromKelvin(Double temp) {
		Double fahrenheitTemp = convertKelvinToFahrenheit(temp);
		return Integer.toString(fahrenheitTemp.intValue());
	}

	public static String convertToDateAndHour(Long dateTime) {
		return dateOnlySimpleDateFormat.format(new Date(dateTime * 1000L));
	}


}
