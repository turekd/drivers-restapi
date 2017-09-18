package it.dturek.github.drivers.service.impl;

import it.dturek.github.drivers.Util;
import it.dturek.github.drivers.domain.CarBrand;
import it.dturek.github.drivers.exception.ResourceNotFoundException;
import it.dturek.github.drivers.repository.CarBrandRepository;
import it.dturek.github.drivers.service.CarBrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarBrandServiceImpl implements CarBrandService {

    @Autowired
    private CarBrandRepository carBrandRepository;

    @Override
    public CarBrand findById(Long id) {
        CarBrand carBrand = carBrandRepository.findOne(id);
        if (carBrand == null) {
            throw new ResourceNotFoundException(String.format("CarBrand with id=%d was not found.", id));
        }
        return carBrand;
    }

    @Override
    public CarBrand create(CarBrand entity) {
        return carBrandRepository.save(entity);
    }

    @Override
    public CarBrand update(CarBrand entity) {
        return carBrandRepository.save(entity);
    }

    @Override
    public void delete(CarBrand entity) {
        carBrandRepository.delete(entity);
    }

    @Override
    public List<CarBrand> findAll() {
        return Util.iteratorToList(carBrandRepository.findAll());
    }

    @Override
    public void deleteAll() {
        carBrandRepository.deleteAll();
    }

    @Override
    public CarBrand findByName(String name) {
        return carBrandRepository.findByName(name);
    }
}
