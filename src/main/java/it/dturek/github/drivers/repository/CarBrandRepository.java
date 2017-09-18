package it.dturek.github.drivers.repository;

import it.dturek.github.drivers.domain.CarBrand;
import org.springframework.data.repository.CrudRepository;

public interface CarBrandRepository extends CrudRepository<CarBrand, Long> {

    CarBrand findByName(String name);

    void deleteAll();

}
