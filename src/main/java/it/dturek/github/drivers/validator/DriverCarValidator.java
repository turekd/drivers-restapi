package it.dturek.github.drivers.validator;

import it.dturek.github.drivers.domain.DriverCar;
import it.dturek.github.drivers.exception.ResourceNotFoundException;
import it.dturek.github.drivers.service.CarModelService;
import it.dturek.github.drivers.service.DriverService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class DriverCarValidator implements Validator {

    private static final Logger LOGGER = LogManager.getLogger(DriverCarValidator.class);

    @Autowired
    private DriverService driverService;

    @Autowired
    private CarModelService carModelService;

    @Override
    public boolean supports(Class<?> aClass) {
        return DriverCar.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        DriverCar form = (DriverCar) o;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "carModel", "carModel.empty", "validation.error.empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "driver", "driver.empty", "validation.error.empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "dateFrom", "dateFrom.empty", "validation.error.empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "dateTo", "dateTo.empty", "validation.error.empty");

        // Check whether dateFrom is not lower than dateTo
        if (form.getDateFrom() != null && form.getDateTo() != null && form.getDateTo().getTime() < form.getDateFrom().getTime()) {
            errors.rejectValue("dateFrom", "dateFrom.invalid", null, "validation.error.dateto_lower_than_datefrom");
        }

        // Check whether driver exists
        if (form.getDriver() != null) {
            try {
                driverService.findById(form.getDriver().getId());
            } catch (ResourceNotFoundException e) {
                errors.rejectValue("driver", "driver.not_exist", null, "validation.error.driver_not_exist");
            }
        }

        // Check whether carModel exists
        if (form.getCarModel() != null) {
            try {
                carModelService.findById(form.getCarModel().getId());
            } catch (ResourceNotFoundException e) {
                errors.rejectValue("carModel", "model.not_exist", null, "validation.error.model_not_exist");
            }
        }
    }
}
