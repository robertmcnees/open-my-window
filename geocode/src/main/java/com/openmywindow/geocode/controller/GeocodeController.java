package com.openmywindow.geocode.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
public class GeocodeController {

	private static final Logger log = LoggerFactory.getLogger(GeocodeController.class);

	@MessageMapping("geocode")
	GeocodeResponse geocode(GeocodeRequest request) {
		log.info("geocode called with " + request.zipcode());

		return new GeocodeResponse(44.4406, 79.9959);
	}
}


record GeocodeRequest(String zipcode) { }

record GeocodeResponse(Double latitude, Double longitude) { }




