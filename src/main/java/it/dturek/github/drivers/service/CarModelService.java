package it.dturek.github.drivers.service;

import it.dturek.github.drivers.domain.CarModel;

public interface CarModelService extends CrudService<CarModel> {

    CarModel findByName(String name);

}
