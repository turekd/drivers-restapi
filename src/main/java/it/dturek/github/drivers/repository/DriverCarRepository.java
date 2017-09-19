package it.dturek.github.drivers.repository;

import it.dturek.github.drivers.domain.Driver;
import it.dturek.github.drivers.domain.DriverCar;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DriverCarRepository extends CrudRepository<DriverCar, Long> {

    List<DriverCar> findAllByDriver(Driver driver);

}
