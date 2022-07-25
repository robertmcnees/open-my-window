package com.openmywindow.forecast.record;

public record HourlyForecast(Long dateTime, Double temp, Double humidity) {}