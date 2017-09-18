package it.dturek.github.drivers.service;

import it.dturek.github.drivers.domain.Driver;

public interface DriverService extends CrudService<Driver> {

    Driver findByLicenseNumber(String licenseNumber);

}
