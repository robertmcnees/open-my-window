package com.openmywindow.geocode.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class GeocodeEntity {

	@Id
	private String postalCode;

	private Double lat;
	private Double lon;

	protected GeocodeEntity() {
	}

	public GeocodeEntity(String postalCode, Double lat, Double lon) {
		this.postalCode = postalCode;
		this.lat = lat;
		this.lon = lon;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	public Double getLon() {
		return lon;
	}

	public void setLon(Double lon) {
		this.lon = lon;
	}
}
