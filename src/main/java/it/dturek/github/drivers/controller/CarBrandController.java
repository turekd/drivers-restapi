package it.dturek.github.drivers.controller;

import it.dturek.github.drivers.domain.CarBrand;
import it.dturek.github.drivers.service.CarBrandService;
import it.dturek.github.drivers.validator.CarBrandValidator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/car_brand")
public class CarBrandController {

    private static final Logger LOGGER = LogManager.getLogger(CarBrandController.class);

    @Autowired
    private CarBrandService carBrandService;

    @Autowired
    private CarBrandValidator carBrandValidator;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(carBrandValidator);
    }

    @PostMapping
    public CarBrand create(@Valid @RequestBody CarBrand carBrand) {
        LOGGER.info("carBrand=" + carBrand);
        return carBrandService.create(carBrand);
    }

    @GetMapping("/{id}")
    public CarBrand findById(@PathVariable("id") Long id) {
        return carBrandService.findById(id);
    }

    @GetMapping
    public List<CarBrand> findAll() {
        return carBrandService.findAll();
    }

    @PutMapping("/{id}")
    public CarBrand update(@PathVariable("id") Long id, @Valid @RequestBody CarBrand newCarBrand) {
        CarBrand carBrand = carBrandService.findById(id);
        carBrand.setName(newCarBrand.getName());
        carBrandService.update(carBrand);
        return carBrand;
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable("id") Long id) {
        CarBrand carBrand = carBrandService.findById(id);
        carBrandService.delete(carBrand);
    }

    @DeleteMapping
    public void deleteAll() {
        carBrandService.deleteAll();
    }

}
