package com.openmywindow.arbiter.service;

import com.openmywindow.arbiter.record.GeocodeCoordinates;

import org.springframework.web.client.RestTemplate;

public class GeocodeService {

	private final RestTemplate restTemplate;

	public GeocodeService(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public GeocodeCoordinates getGeocodeCoordinates(String postalCode) {
		return restTemplate.getForObject("http://localhost:8081/geocode/coordinates?postalCode=" + postalCode, GeocodeCoordinates.class);
	}
}
