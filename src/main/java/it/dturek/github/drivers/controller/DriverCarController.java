package it.dturek.github.drivers.controller;

import it.dturek.github.drivers.domain.DriverCar;
import it.dturek.github.drivers.service.DriverCarService;
import it.dturek.github.drivers.validator.DriverCarValidator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/driver_car")
public class DriverCarController {

    private static final Logger LOGGER = LogManager.getLogger(DriverCarController.class);
    private final String MODEL_ATTRIBUTE_UPDATE = "driverCarUpdate";

    @Autowired
    private DriverCarService driverCarService;

    @Autowired
    private DriverCarValidator driverCarValidator;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(driverCarValidator);
    }

    @ModelAttribute(MODEL_ATTRIBUTE_UPDATE)
    DriverCar getDriverCar(@RequestBody(required = false) DriverCar driverCar) {
        return driverCar;
    }

    @GetMapping
    public List<DriverCar> findAll() {
        return driverCarService.findAll();
    }

    @GetMapping("/{id}")
    public DriverCar findById(@PathVariable("id") Long id) {
        return driverCarService.findById(id);
    }

    @PostMapping
    public DriverCar create(@Valid @RequestBody DriverCar driverCar) {
        return driverCarService.create(driverCar);
    }

    @PutMapping("/{id}")
    public DriverCar update(@PathVariable("id") Long id, @Valid @ModelAttribute(MODEL_ATTRIBUTE_UPDATE) DriverCar driverCar) {
        driverCarService.findById(id);
        return driverCarService.update(driverCar);
    }

    @DeleteMapping
    public void deleteAll() {
        driverCarService.deleteAll();
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable("id") Long id) {
        DriverCar driverCar = driverCarService.findById(id);
        driverCarService.delete(driverCar);
    }

}
