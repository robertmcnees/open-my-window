package com.openmywindow.arbiter.domain;

public class CurrentWeather {

	private Double currentTemp;
	private Double dailyHighTemp;
	private Double dailyLowTemp;

	public CurrentWeather(Double currentTemp, Double dailyHighTemp, Double dailyLowTemp){
		this.currentTemp = currentTemp;
		this.dailyHighTemp = dailyHighTemp;
		this.dailyLowTemp = dailyLowTemp;
	}

	public Double getCurrentTemp() {
		return currentTemp;
	}

	public void setCurrentTemp(Double currentTemp) {
		this.currentTemp = currentTemp;
	}

	public Double getDailyHighTemp() {
		return dailyHighTemp;
	}

	public void setDailyHighTemp(Double dailyHighTemp) {
		this.dailyHighTemp = dailyHighTemp;
	}

	public Double getDailyLowTemp() {
		return dailyLowTemp;
	}

	public void setDailyLowTemp(Double dailyLowTemp) {
		this.dailyLowTemp = dailyLowTemp;
	}
}
