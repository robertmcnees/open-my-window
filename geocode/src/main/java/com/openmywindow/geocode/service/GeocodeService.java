package com.openmywindow.geocode.service;

import com.openmywindow.geocode.configuration.OpenWeatherVaultConfiguration;
import com.openmywindow.geocode.entity.GeocodeEntity;
import com.openmywindow.geocode.record.GeocodeResponse;
import com.openmywindow.geocode.record.OpenWeatherGeocodeResponse;
import com.openmywindow.geocode.repository.GeocodeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.web.client.RestTemplate;

public class GeocodeService {

	private static final Logger log = LoggerFactory.getLogger(GeocodeService.class);
	private final String OPEN_WEATHER_API_KEY;
	private final RestTemplate restTemplate;
	private final GeocodeRepository geocodeRepository;

	public GeocodeService(RestTemplate restTemplate, OpenWeatherVaultConfiguration openWeatherVaultConfiguration, GeocodeRepository geocodeRepository) {
		this.restTemplate = restTemplate;
		OPEN_WEATHER_API_KEY = openWeatherVaultConfiguration.getApikey();
		this.geocodeRepository = geocodeRepository;
	}

	public GeocodeResponse geocodeZipcode(String postalCode) {
		GeocodeEntity cached = findCachedValue(postalCode);
		if (cached == null) {
			log.info("no cache for " + postalCode + ", calling open weather api");
			OpenWeatherGeocodeResponse response =
					restTemplate.getForObject(
							"http://api.openweathermap.org/geo/1.0/zip?zip=" + postalCode + ",US&appid=" + OPEN_WEATHER_API_KEY,
							OpenWeatherGeocodeResponse.class);
			log.info("Postal Code " + postalCode + " coordinates of lat: " + response.lat() + " & lon: " + response.lon());
			geocodeRepository.save(new GeocodeEntity(response.zip(), response.lat(), response.lon()));
			return new GeocodeResponse(response.lat(), response.lon());
		}
		else {
			log.info("returning " + postalCode + " from cache:  lat=" + cached.getLat() + " lon=" + cached.getLon());
			return new GeocodeResponse(cached.getLat(), cached.getLon());
		}
	}

	private GeocodeEntity findCachedValue(String postalCode) {
		return geocodeRepository.findByPostalCode(postalCode);
	}
}
