package com.openmywindow.arbiter.domain;

public class Window {

	private String status;

	public Window() {
		this.status = "undefined";
	}

	public Window(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
