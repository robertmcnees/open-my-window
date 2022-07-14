package com.openmywindow.forecast.helper;

public class Conversion {

	public static Double convertKelvinToFahrenheit(Double temp) {
		return (temp - 273.15) * 9 / 5 + 32;
	}
}
