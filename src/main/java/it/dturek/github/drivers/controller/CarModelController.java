package it.dturek.github.drivers.controller;

import it.dturek.github.drivers.domain.CarModel;
import it.dturek.github.drivers.service.CarBrandService;
import it.dturek.github.drivers.service.CarModelService;
import it.dturek.github.drivers.validator.CarModelValidator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/car_model")
public class CarModelController {

    private static final Logger LOGGER = LogManager.getLogger(CarModelController.class);

    @Autowired
    private CarModelService carModelService;

    @Autowired
    private CarBrandService carBrandService;

    @Autowired
    private CarModelValidator carModelValidator;

    @InitBinder
    protected void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(carModelValidator);
    }

    @PostMapping
    public CarModel create(@Valid @RequestBody CarModel carModel) {
        LOGGER.info("*** create *** " + carModel);
        return carModelService.create(carModel);
    }

    @GetMapping
    public List<CarModel> findAll() {
        return carModelService.findAll();
    }

    @PutMapping("/{id}")
    public CarModel update(@PathVariable("id") Long id, @Valid @RequestBody CarModel carModel) {
        LOGGER.info("*** update *** " + carModel);
        carModelService.findById(id);
        carModel.setId(id);
        carModel.setBrand(carBrandService.findById(carModel.getBrand().getId()));
        return carModelService.update(carModel);
    }

    @GetMapping("/{id}")
    public CarModel findById(@PathVariable("id") Long id) {
        return carModelService.findById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable("id") Long id) {
        CarModel carModel = carModelService.findById(id);
        carModelService.delete(carModel);
    }

    @DeleteMapping
    public void deleteAll() {
        carModelService.deleteAll();
    }

}
