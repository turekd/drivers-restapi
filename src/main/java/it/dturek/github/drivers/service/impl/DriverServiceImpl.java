package it.dturek.github.drivers.service.impl;

import it.dturek.github.drivers.Util;
import it.dturek.github.drivers.domain.Driver;
import it.dturek.github.drivers.exception.ResourceNotFoundException;
import it.dturek.github.drivers.repository.DriverRepository;
import it.dturek.github.drivers.service.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;

@Service
public class DriverServiceImpl implements DriverService {

    @Autowired
    private DriverRepository driverRepository;

    @Override
    public Driver findByLicenseNumber(String licenseNumber) {
        return driverRepository.findByLicenseNumber(licenseNumber);
    }

    @Override
    public Driver findById(Long id) {
        Driver driver = driverRepository.findOne(id);
        if (driver == null) {
            throw new ResourceNotFoundException(String.format("Driver with id=%d was not found.", id));
        }
        return driver;
    }

    @Override
    public Driver create(Driver entity) {
        return driverRepository.save(entity);
    }

    @Override
    public Driver update(Driver entity) {
        return driverRepository.save(entity);
    }

    @Override
    public void delete(Driver entity) {
        driverRepository.delete(entity);
    }

    @Override
    public List<Driver> findAll() {
        return Util.iteratorToList(driverRepository.findAll());
    }

    @Override
    public void deleteAll() {
        driverRepository.deleteAll();
    }
}
