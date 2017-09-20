package it.dturek.github.drivers.repository;

import it.dturek.github.drivers.domain.CarBrand;
import it.dturek.github.drivers.domain.CarModel;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CarModelRepository extends CrudRepository<CarModel, Long> {

    CarModel findOneByName(String name);

    List<CarModel> findAllByBrand(CarBrand carBrand);

}
