package com.openmywindow.arbiter.service;

import com.openmywindow.arbiter.record.GeocodeCoordinates;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class GeocodeService {

	private static final Logger log = LoggerFactory.getLogger(RestTemplate.class);
	private final RestTemplate restTemplate;
	@Value("${geocodeserviceurl:geocode}")
	private String geocodeServiceUrl;

	public GeocodeService(RestTemplateBuilder restTemplateBuilder) {
		this.restTemplate = restTemplateBuilder.build();
	}

	public GeocodeCoordinates getGeocodeCoordinates(String postalCode) {
		log.info("Geocode URL" + geocodeServiceUrl);
		return restTemplate.getForObject("http://" + geocodeServiceUrl + "/geocode/coordinates?postalCode=" + postalCode, GeocodeCoordinates.class);
	}
}
