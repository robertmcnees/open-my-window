package com.openmywindow.geocode.service;

import com.azure.security.keyvault.secrets.SecretClient;
import com.openmywindow.geocode.entity.GeocodeEntity;
import com.openmywindow.geocode.record.GeocodeResponse;
import com.openmywindow.geocode.record.OpenWeatherGeocodeResponse;
import com.openmywindow.geocode.repository.GeocodeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class GeocodeService {

	private final WebClient webClient;
	private final GeocodeRepository geocodeRepository;
	private final Logger log = LoggerFactory.getLogger(GeocodeService.class);
	private final SecretClient secretClient;

	@Value("${SECRET_OPENWEATHER_API_KEY:defaultValue}")
	private String OPEN_WEATHER_API_KEY;

	private String OPEN_WEATHER_API_KEY_FROM_VAULT;
	private boolean foundKeyVault = false;

	public GeocodeService(WebClient.Builder builder, GeocodeRepository geocodeRepository, SecretClient secretClient) {
		this.webClient = builder.baseUrl("http://api.openweathermap.org/geo/1.0").build();
		this.geocodeRepository = geocodeRepository;
		this.secretClient = secretClient;
		try {
			OPEN_WEATHER_API_KEY_FROM_VAULT = secretClient.getSecret("OPENWEATHERAPIKEY").getValue();
			foundKeyVault = true;
		} catch (Exception e) {
			System.out.println("Error connecting to Azure Key Vault.  Use default API key");
			System.out.println(e);
		}
	}

	public Mono<GeocodeResponse> geocodePostalCode(String postalCode, String countryCode) {
		return reactiveFindCachedValue(postalCode)
				.switchIfEmpty(Mono.defer(() -> this.reactiveWebCall(postalCode, countryCode)));
	}

	private Mono<GeocodeResponse> reactiveWebCall(String postalCode, String countryCode) {
		return webClient
				.get()
				.uri("/zip?zip=" + postalCode + "," + countryCode + "&appid=" + ((foundKeyVault) ? OPEN_WEATHER_API_KEY_FROM_VAULT : OPEN_WEATHER_API_KEY))/*OPEN_WEATHER_API_KEY)*/ //secretClient.getSecret("OPENWEATHERAPIKEY"))
				.accept(MediaType.APPLICATION_JSON)
				.retrieve()
				.bodyToMono(OpenWeatherGeocodeResponse.class)
				.map(r -> new GeocodeResponse(r.lat(), r.lon()))
				.map(r -> reactiveSave(postalCode, r));
	}

	private GeocodeResponse reactiveSave(String postalCode, GeocodeResponse response) {
		geocodeRepository.save(new GeocodeEntity(postalCode, response.lat(), response.lon()));
		return response;
	}

	private Mono<GeocodeResponse> reactiveFindCachedValue(String postalCode) {
		Mono<GeocodeEntity> databaseEntity = geocodeRepository.findByPostalCode(postalCode);
		return (databaseEntity == null) ? Mono.empty() : databaseEntity.map(r -> new GeocodeResponse(r.getLat(), r.getLon()));
	}

}
