package com.openmywindow.arbiter.controller;

import com.openmywindow.arbiter.record.DailyWeatherRecord;
import com.openmywindow.arbiter.record.WindowRecord;
import com.openmywindow.arbiter.service.WeatherService;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ArbiterControllerTests {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@MockBean
	private WeatherService weatherService;

	@Test
	public void testMethodShouldReturnValue() {
		String response = this.restTemplate.getForObject("http://localhost:" + port + "/arbiter/test",
				String.class);
		assertEquals(response, "hello window");
	}

	@Test
	public void testWithMockWeatherService() {
		when(weatherService.getDailyWeather(anyString())).thenReturn(new DailyWeatherRecord(60, 81, 54));
		WindowRecord window = this.restTemplate.getForObject("http://localhost:" + port + "/arbiter/myWindow", WindowRecord.class);
		assertEquals("open - cold now, hot later", window.status());
	}

}
