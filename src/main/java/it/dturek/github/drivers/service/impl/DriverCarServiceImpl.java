package it.dturek.github.drivers.service.impl;

import it.dturek.github.drivers.Util;
import it.dturek.github.drivers.domain.Driver;
import it.dturek.github.drivers.domain.DriverCar;
import it.dturek.github.drivers.exception.ResourceNotFoundException;
import it.dturek.github.drivers.repository.DriverCarRepository;
import it.dturek.github.drivers.service.DriverCarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DriverCarServiceImpl implements DriverCarService {

    @Autowired
    private DriverCarRepository driverCarRepository;

    @Override
    public DriverCar findById(Long id) {
        DriverCar driverCar = driverCarRepository.findOne(id);
        if (driverCar == null) {
            throw new ResourceNotFoundException(String.format("DriverCar with id=%d was not found.", id));
        }
        return driverCar;
    }

    @Override
    public DriverCar create(DriverCar entity) {
        return driverCarRepository.save(entity);
    }

    @Override
    public DriverCar update(DriverCar entity) {
        return driverCarRepository.save(entity);
    }

    @Override
    public void delete(DriverCar entity) {
        driverCarRepository.delete(entity);
    }

    @Override
    public List<DriverCar> findAll() {
        return Util.iteratorToList(driverCarRepository.findAll());
    }

    @Override
    public void deleteAll() {
        driverCarRepository.deleteAll();
    }

    @Override
    public List<DriverCar> findAllByDriver(Driver driver) {
        return driverCarRepository.findAllByDriver(driver);
    }
}
