package com.openmywindow.forecast.record;

import java.util.List;

public record ForecastResponse(Double temp, Double minTemp, Double maxTemp, Double humidity,
							   List<DailyForecast> dailyForecastList, List<HourlyForecast> hourlyForecastList){
}

