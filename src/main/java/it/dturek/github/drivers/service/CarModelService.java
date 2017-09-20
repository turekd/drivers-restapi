package it.dturek.github.drivers.service;

import it.dturek.github.drivers.domain.CarBrand;
import it.dturek.github.drivers.domain.CarModel;

import java.util.List;

public interface CarModelService extends CrudService<CarModel> {

    CarModel findByName(String name);

    List<CarModel> findAllByBrand(CarBrand carBrand);

}
