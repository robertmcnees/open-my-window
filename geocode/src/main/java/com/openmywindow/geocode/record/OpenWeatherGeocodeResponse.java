package com.openmywindow.geocode.record;

public record OpenWeatherGeocodeResponse(String zip, String name, Double lat, Double lon, String countrys) {
}
