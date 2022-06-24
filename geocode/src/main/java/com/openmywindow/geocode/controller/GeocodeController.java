package com.openmywindow.geocode.controller;

import com.openmywindow.geocode.record.GeocodeResponse;
import com.openmywindow.geocode.service.GeocodeService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("geocode")
public class GeocodeController {

	private final GeocodeService geocodeService;

	public GeocodeController(GeocodeService geocodeService) {
		this.geocodeService = geocodeService;
	}

	@GetMapping("coordinates")
	public GeocodeResponse getCoordinates(@RequestParam String postalCode) {
		return geocodeService.geocodePostalCode(postalCode);
	}
}
