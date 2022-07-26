package com.openmywindow.arbiter.service;

import com.openmywindow.arbiter.record.GeocodeCoordinates;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class GeocodeService {

	private final RestTemplate restTemplate;
	private static final Logger log = LoggerFactory.getLogger(RestTemplate.class);
	private final DiscoveryClient discoveryClient;

	public GeocodeService(RestTemplateBuilder restTemplateBuilder, DiscoveryClient discoveryClient) {
		this.restTemplate = restTemplateBuilder.build();
		this.discoveryClient = discoveryClient;
	}

	public GeocodeCoordinates getGeocodeCoordinates(String postalCode) {
		ServiceInstance serviceInstance = discoveryClient.getInstances("geocode").get(0);
		log.info("Geocode URL" + serviceInstance.getUri().toString());
		return restTemplate.getForObject("http://" + serviceInstance.getUri().getHost() + ":" + serviceInstance.getUri().getPort()
				+ "/geocode/coordinates?postalCode=" + postalCode, GeocodeCoordinates.class);
	}
}
