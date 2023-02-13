package com.openmywindow.arbiter.record;

public record WindowStatus(String status, CurrentWeather currentWeather, WindowChange nextWindowChange) {
}
