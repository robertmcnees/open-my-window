package com.openmywindow.geocode.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class GeocodeEntity {

	@Id
	private String zipcode;
	private Double lat;
	private Double lon;

	protected GeocodeEntity() {}

	public GeocodeEntity(String zipcode, Double lat, Double lon) {
		this.zipcode = zipcode;
		this.lat = lat;
		this.lon = lon;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
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
