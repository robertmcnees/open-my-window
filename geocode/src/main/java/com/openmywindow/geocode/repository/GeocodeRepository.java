package com.openmywindow.geocode.repository;

import com.openmywindow.geocode.entity.GeocodeEntity;

import org.springframework.data.repository.CrudRepository;

public interface GeocodeRepository extends CrudRepository<GeocodeEntity, String> {

	GeocodeEntity findByZipcode(String zipcode);

}
