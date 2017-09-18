package it.dturek.github.drivers.service;

import it.dturek.github.drivers.domain.CarBrand;

public interface CarBrandService extends CrudService<CarBrand> {

    CarBrand findByName(String name);

}
