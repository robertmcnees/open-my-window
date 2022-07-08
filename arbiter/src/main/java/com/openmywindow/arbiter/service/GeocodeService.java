package com.openmywindow.arbiter.service;

import com.openmywindow.arbiter.record.GeocodeCoordinates;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

public class GeocodeService {

	private final RestTemplate restTemplate;
	private static final Logger log = LoggerFactory.getLogger(RestTemplate.class);

	@Value("${geocodeserviceurl}")
	private String geocodeServiceUrl;

	public GeocodeService(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public GeocodeCoordinates getGeocodeCoordinates(String postalCode) {
		log.info("Geocode URL" + geocodeServiceUrl);
		return restTemplate.getForObject("http://" + geocodeServiceUrl + "/geocode/coordinates?postalCode=" + postalCode, GeocodeCoordinates.class);
	}
}
