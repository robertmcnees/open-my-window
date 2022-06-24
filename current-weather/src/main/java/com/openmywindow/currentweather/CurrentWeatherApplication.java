package com.openmywindow.currentweather;

import com.openmywindow.currentweather.configuration.OpenWeatherVaultConfiguration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(OpenWeatherVaultConfiguration.class)
public class CurrentWeatherApplication {

	public static void main(String[] args) {
		SpringApplication.run(CurrentWeatherApplication.class, args);
	}

}
