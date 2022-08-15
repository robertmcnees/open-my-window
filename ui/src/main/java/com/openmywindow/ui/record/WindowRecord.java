package com.openmywindow.ui.record;

public record WindowRecord(String status, String tempRecommendation, String humidityRecommendation, String nextRecommendation, Double currentTemp, Double currentHumidity, Double dailyHighTemp, Double dailyLowTemp) { }
