package com.openmywindow.geocode.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("openweatherapi")
public class OpenWeatherVaultConfiguration {

	private String apikey;

	public String getApikey() {
		return apikey;
	}

	public void setApikey(String apikey) {
		this.apikey = apikey;
	}
}
