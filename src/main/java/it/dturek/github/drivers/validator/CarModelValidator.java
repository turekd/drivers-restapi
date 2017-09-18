package it.dturek.github.drivers.validator;

import it.dturek.github.drivers.domain.CarModel;
import it.dturek.github.drivers.exception.ResourceNotFoundException;
import it.dturek.github.drivers.service.CarBrandService;
import it.dturek.github.drivers.service.CarModelService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class CarModelValidator implements Validator {

    private static final Logger LOGGER = LogManager.getLogger(CarModelValidator.class);

    @Autowired
    private CarBrandService carBrandService;

    @Autowired
    private CarModelService carModelService;

    @Override
    public boolean supports(Class<?> aClass) {
        return CarModel.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object object, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "name.empty", "validation.error.empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "brand", "name.empty", "validation.error.empty");

        CarModel form = (CarModel) object;

        // Check whether brand exist
        if (form.getBrand() != null) {
            try {
                carBrandService.findById(form.getBrand().getId());
            } catch (ResourceNotFoundException e) {
                errors.rejectValue("brand", "name.not_exist", null, "validation.error.brand_not_exist");
            }
        }

        // Check whether name is unique
        CarModel modelWithSameName = carModelService.findByName(form.getName());
        if (form.getBrand() != null && modelWithSameName != null && modelWithSameName.getBrand().getId().equals(form.getBrand().getId())) {
            errors.rejectValue("name", "brand.duplicated", null, "validation.error.model_duplicated");
        }

    }
}
