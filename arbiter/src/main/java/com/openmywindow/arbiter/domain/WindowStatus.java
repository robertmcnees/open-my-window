package com.openmywindow.arbiter.domain;

public class WindowStatus {


	private String status;

	private CurrentWeather currentWeather;

	private WindowChange windowChange;

	public WindowStatus(String status, CurrentWeather currentWeather, WindowChange windowChange) {
		this.status = status;
		this.currentWeather = currentWeather;
		this.windowChange = windowChange;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public CurrentWeather getCurrentWeather() {
		return currentWeather;
	}

	public void setCurrentWeather(CurrentWeather currentWeather) {
		this.currentWeather = currentWeather;
	}

	public WindowChange getWindowChange() {
		return windowChange;
	}

	public void setWindowChange(WindowChange windowChange) {
		this.windowChange = windowChange;
	}
}
