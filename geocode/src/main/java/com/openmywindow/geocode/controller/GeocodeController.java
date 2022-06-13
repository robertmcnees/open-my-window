package com.openmywindow.geocode.controller;


import com.openmywindow.geocode.record.GeocodeRequest;
import com.openmywindow.geocode.record.GeocodeResponse;
import com.openmywindow.geocode.service.GeocodeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
public class GeocodeController {

	private static final Logger log = LoggerFactory.getLogger(GeocodeController.class);

	private GeocodeService geocodeService;

	public GeocodeController(GeocodeService geocodeService) {
		this.geocodeService = geocodeService;
	}


	@MessageMapping("geocode")
	GeocodeResponse geocode(GeocodeRequest request) {
		log.info("geocode called with " + request.postalCode());
		return geocodeService.geocodeZipcode(request.postalCode());

	}
}



