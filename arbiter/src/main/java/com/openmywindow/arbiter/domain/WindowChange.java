package com.openmywindow.arbiter.domain;

import java.text.SimpleDateFormat;
import java.util.Date;

public class WindowChange {

	private String status;
	private Long dateTimeChange;
	private String dateTimeReadable;
	private Double temp;

	public WindowChange(String status, Long dateTimeChange, Double temp) {
		this.status = status;
		this.dateTimeChange = dateTimeChange;
		this.temp = temp;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getDateTimeChange() {
		return dateTimeChange;
	}

	public void setDateTimeChange(Long dateTimeChange) {
		this.dateTimeChange = dateTimeChange;
	}

	public Double getTemp() {
		return temp;
	}

	public void setTemp(Double temp) {
		this.temp = temp;
	}

	public String getDateTimeReadable() {
		if(dateTimeChange == null) return null;

		Date date = new Date(dateTimeChange);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd hh a z");
		return simpleDateFormat.format(date);
	}
}
