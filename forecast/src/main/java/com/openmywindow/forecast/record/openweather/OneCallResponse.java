package com.openmywindow.forecast.record.openweather;

import java.util.List;

import com.openmywindow.forecast.record.openweather.current.OpenWeatherCurrent;
import com.openmywindow.forecast.record.openweather.daily.OpenWeatherDaily;
import com.openmywindow.forecast.record.openweather.hourly.OpenWeatherHourly;

public record OneCallResponse (OpenWeatherCurrent current, List<OpenWeatherDaily> daily, List<OpenWeatherHourly> hourly) {
}
