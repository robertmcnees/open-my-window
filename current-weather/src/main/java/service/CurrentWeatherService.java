package service;

import com.openmywindow.currentweather.configuration.OpenWeatherVaultConfiguration;
import com.openmywindow.currentweather.helper.Conversion;
import com.openmywindow.currentweather.record.CurrentWeatherResponse;
import com.openmywindow.currentweather.record.OpenWeatherApiCurrentWeatherResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.web.client.RestTemplate;

public class CurrentWeatherService {

	private static final Logger log = LoggerFactory.getLogger(CurrentWeatherService.class);
	private final String OPEN_WEATHER_API_KEY;
	private final RestTemplate restTemplate;

	public CurrentWeatherService(RestTemplate restTemplate, OpenWeatherVaultConfiguration vaultConfiguration) {
		this.restTemplate = restTemplate;
		OPEN_WEATHER_API_KEY = vaultConfiguration.getApikey();
	}

	public CurrentWeatherResponse getOpenWeatherApiCurrentWeather(Double lat, Double lon) {

		OpenWeatherApiCurrentWeatherResponse response =
				restTemplate.getForObject(
						"https://api.openweathermap.org/data/2.5/weather?lat=" + lat + "&lon=" + lon + "&appid=" + OPEN_WEATHER_API_KEY,
						OpenWeatherApiCurrentWeatherResponse.class);

		return new CurrentWeatherResponse(
				Conversion.convertKelvinToFahrenheit(response.main().temp()),
				Conversion.convertKelvinToFahrenheit(response.main().temp_min()),
				Conversion.convertKelvinToFahrenheit(response.main().temp_max()));

	}

}
