package it.dturek.github.drivers.validator;

import it.dturek.github.drivers.domain.Driver;
import it.dturek.github.drivers.service.DriverService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class DriverValidator implements Validator {

    private static final Logger LOGGER = LogManager.getLogger(DriverValidator.class);

    @Autowired
    private DriverService driverService;

    @Override
    public boolean supports(Class<?> aClass) {
        return Driver.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object object, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "firstName", "firstName.empty", "validation.error.empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "lastName", "lastName.empty", "validation.error.empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "licenseNumber", "licenseNumber.empty", "validation.error.empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "dateOfBirth", "dateOfBirth.empty", "validation.error.empty");

        Driver form = (Driver) object;

        // Check whether license number is unique
        Driver driverWithSameLicenseNumber = driverService.findByLicenseNumber(form.getLicenseNumber());
        if (driverWithSameLicenseNumber != null
                && ((form.getId() != null && !driverWithSameLicenseNumber.getId().equals(form.getId())) || form.getId() == null)) {
            errors.rejectValue("licenseNumber", "licenseNumber.not_unique", null, "validation.error.not_unique");
        }
    }
}
