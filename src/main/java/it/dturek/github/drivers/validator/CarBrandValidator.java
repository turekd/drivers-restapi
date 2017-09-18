package it.dturek.github.drivers.validator;

import it.dturek.github.drivers.domain.CarBrand;
import it.dturek.github.drivers.service.CarBrandService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class CarBrandValidator implements Validator {

    private static final Logger LOGGER = LogManager.getLogger(CarBrandValidator.class);

    @Autowired
    private CarBrandService carBrandService;

    @Override
    public boolean supports(Class<?> aClass) {
        return CarBrand.class.equals(aClass);
    }

    @Override
    public void validate(Object object, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "name.empty", "validation.error.empty");
        CarBrand carBrand = (CarBrand) object;

        if (carBrandService.findByName(carBrand.getName()) != null) {
            errors.rejectValue("name", "name.not_unique", null, "validation.error.not_unique");
        }
    }

}
