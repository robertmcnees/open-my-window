package com.openmywindow.arbiter.service;

import com.openmywindow.arbiter.record.ForecastRecord;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ForecastService {

	private final RestTemplate restTemplate;
	private final DiscoveryClient discoveryClient;

	public ForecastService(RestTemplateBuilder restTemplateBuilder, DiscoveryClient discoveryClient) {
		this.restTemplate = restTemplateBuilder.build();
		this.discoveryClient = discoveryClient;
	}

	public ForecastRecord getCurrentWeather(Double lat, Double lon) {
		ServiceInstance serviceInstance = discoveryClient.getInstances("forecast").get(0);
		return restTemplate.getForObject("http://" + serviceInstance.getUri().getHost() + ":" + serviceInstance.getUri().getPort()
				+ "/forecast/forecastWeather?lat="+lat+"&lon="+lon, ForecastRecord.class);
	}
}
