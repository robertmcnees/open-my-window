package com.openmywindow.arbiter.domain;

public class WindowRecommendation {

	private String status;
	private String tempRecommendation;
	private String nextRecommendation;
	private Double currentTemp;
	private Double dailyHighTemp;
	private Double dailyLowTemp;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTempRecommendation() {
		return tempRecommendation;
	}

	public void setTempRecommendation(String tempRecommendation) {
		this.tempRecommendation = tempRecommendation;
	}

	public String getNextRecommendation() {
		return nextRecommendation;
	}

	public void setNextRecommendation(String nextRecommendation) {
		this.nextRecommendation = nextRecommendation;
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
