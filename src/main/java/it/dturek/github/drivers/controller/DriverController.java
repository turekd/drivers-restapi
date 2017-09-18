package it.dturek.github.drivers.controller;

import it.dturek.github.drivers.domain.Driver;
import it.dturek.github.drivers.service.DriverService;
import it.dturek.github.drivers.validator.DriverValidator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/driver")
public class DriverController {

    private static final Logger LOGGER = LogManager.getLogger(DriverController.class);
    private final String MODEL_ATTRIBUTE_UPDATE = "driver";

    @Autowired
    private DriverService driverService;

    @Autowired
    private DriverValidator driverValidator;

    @InitBinder
    protected void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(driverValidator);
    }

    @ModelAttribute(MODEL_ATTRIBUTE_UPDATE)
    Driver getDriver(@RequestBody(required = false) Driver driver) {
        return driver;
    }

    @GetMapping
    public List<Driver> findAll() {
        return driverService.findAll();
    }

    @GetMapping("/{id}")
    public Driver findById(@PathVariable("id") Long id) {
        return driverService.findById(id);
    }

    @DeleteMapping
    public void deleteAll() {
        driverService.deleteAll();
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable("id") Long id) {
        Driver driver = driverService.findById(id);
        driverService.delete(driver);
    }

    @PostMapping
    public Driver create(@Valid @RequestBody Driver driver) {
        return driverService.create(driver);
    }

    @PutMapping("/{id}")
    public Driver update(@PathVariable("id") Long id, @Valid @ModelAttribute(MODEL_ATTRIBUTE_UPDATE) Driver driver) {
        driverService.findById(id);
        return driverService.update(driver);
    }

}
