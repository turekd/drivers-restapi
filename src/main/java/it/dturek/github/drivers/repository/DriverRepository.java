package it.dturek.github.drivers.repository;

import it.dturek.github.drivers.domain.Driver;
import org.springframework.data.repository.CrudRepository;

public interface DriverRepository extends CrudRepository<Driver, Long> {

    Driver findByLicenseNumber(String licenseNumber);

}
