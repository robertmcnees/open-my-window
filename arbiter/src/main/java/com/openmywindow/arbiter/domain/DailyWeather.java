package com.openmywindow.arbiter.domain;

public class DailyWeather {

	private Integer currentTemp;
	private Integer highTemp;
	private Integer lowTemp;
	public DailyWeather(Integer currentTemp, Integer highTemp, Integer lowTemp) {
		this.currentTemp = currentTemp;
		this.highTemp = highTemp;
		this.lowTemp = lowTemp;
	}

	public Integer getCurrentTemp() {
		return currentTemp;
	}

	public void setCurrentTemp(Integer currentTemp) {
		this.currentTemp = currentTemp;
	}

	public Integer getHighTemp() {
		return highTemp;
	}

	public void setHighTemp(Integer highTemp) {
		this.highTemp = highTemp;
	}

	public Integer getLowTemp() {
		return lowTemp;
	}

	public void setLowTemp(Integer lowTemp) {
		this.lowTemp = lowTemp;
	}
}
