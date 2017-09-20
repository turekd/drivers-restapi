package it.dturek.github.drivers.service.impl;

import it.dturek.github.drivers.Util;
import it.dturek.github.drivers.domain.CarBrand;
import it.dturek.github.drivers.domain.CarModel;
import it.dturek.github.drivers.exception.ResourceNotFoundException;
import it.dturek.github.drivers.repository.CarModelRepository;
import it.dturek.github.drivers.service.CarModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarModelServiceImpl implements CarModelService {

    @Autowired
    private CarModelRepository carModelRepository;

    @Override
    public CarModel findById(Long id) {
        CarModel result = carModelRepository.findOne(id);
        if (result == null) {
            throw new ResourceNotFoundException(String.format("CarModel with id=%d was not found.", id));
        }
        return result;
    }

    @Override
    public CarModel create(CarModel entity) {
        return carModelRepository.save(entity);
    }

    @Override
    public CarModel update(CarModel entity) {
        return carModelRepository.save(entity);
    }

    @Override
    public void delete(CarModel entity) {
        carModelRepository.delete(entity);
    }

    @Override
    public List<CarModel> findAll() {
        return Util.iteratorToList(carModelRepository.findAll());
    }

    @Override
    public void deleteAll() {
        carModelRepository.deleteAll();
    }

    @Override
    public CarModel findByName(String name) {
        return carModelRepository.findOneByName(name);
    }

    @Override
    public List<CarModel> findAllByBrand(CarBrand carBrand) {
        return carModelRepository.findAllByBrand(carBrand);
    }
}
