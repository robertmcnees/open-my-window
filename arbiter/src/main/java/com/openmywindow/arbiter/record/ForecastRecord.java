package com.openmywindow.arbiter.record;

import java.util.List;

public record ForecastRecord(Double temp, Double minTemp, Double maxTemp, List<HourlyForecastRecord> hourlyForecastList) { }
