package it.dturek.github.drivers.repository;

import it.dturek.github.drivers.domain.CarModel;
import org.springframework.data.repository.CrudRepository;

public interface CarModelRepository extends CrudRepository<CarModel, Long> {

    CarModel findOneByName(String name);

}
