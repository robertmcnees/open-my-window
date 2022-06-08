package com.openmywindow.geocode.service;

import com.openmywindow.geocode.configuration.OpenWeatherVaultConfiguration;
import com.openmywindow.geocode.record.GeocodeResponse;
import com.openmywindow.geocode.record.OpenWeatherGeocodeResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.web.client.RestTemplate;

public class GeocodeService {

	private final String OPEN_WEATHER_API_KEY;
	private static final Logger log = LoggerFactory.getLogger(GeocodeService.class);
	private final RestTemplate restTemplate;

	public GeocodeService(RestTemplate restTemplate, OpenWeatherVaultConfiguration openWeatherVaultConfiguration) {
		this.restTemplate = restTemplate;
		OPEN_WEATHER_API_KEY = openWeatherVaultConfiguration.getApikey();
	}

	public GeocodeResponse geocodeZipcode(String zipcode) {
		OpenWeatherGeocodeResponse response =
				restTemplate.getForObject(
						"http://api.openweathermap.org/geo/1.0/zip?zip=" + zipcode + ",US&appid=" + OPEN_WEATHER_API_KEY,
						OpenWeatherGeocodeResponse.class);
		log.info("Zip " + zipcode + " coordinates of lat: " + response.lat() + " & lon: " + response.lon());
		return new GeocodeResponse(response.lat(), response.lon());
	}

}
