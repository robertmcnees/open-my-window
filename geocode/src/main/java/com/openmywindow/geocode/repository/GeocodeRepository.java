package com.openmywindow.geocode.repository;

import com.openmywindow.geocode.entity.GeocodeEntity;
import reactor.core.publisher.Mono;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface GeocodeRepository extends ReactiveCrudRepository<GeocodeEntity, String> {

	Mono<GeocodeEntity> findByPostalCode(String postalCode);

}
