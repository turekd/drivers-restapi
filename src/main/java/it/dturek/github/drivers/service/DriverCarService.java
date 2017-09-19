package it.dturek.github.drivers.service;

import it.dturek.github.drivers.domain.Driver;
import it.dturek.github.drivers.domain.DriverCar;

import java.util.List;

public interface DriverCarService extends CrudService<DriverCar> {

    List<DriverCar> findAllByDriver(Driver driver);

}
