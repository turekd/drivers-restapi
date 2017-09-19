package it.dturek.github.drivers.validator;

import it.dturek.github.drivers.domain.CarBrand;
import it.dturek.github.drivers.domain.CarModel;
import it.dturek.github.drivers.exception.ResourceNotFoundException;
import it.dturek.github.drivers.service.CarBrandService;
import it.dturek.github.drivers.service.CarModelService;
import org.easymock.EasyMockRunner;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.validation.Errors;

import static org.easymock.EasyMock.*;

@RunWith(EasyMockRunner.class)
public class CarModelValidatorTest {

    @TestSubject
    private CarModelValidator carModelValidator = new CarModelValidator();

    @Mock
    private CarBrandService carBrandService;

    @Mock
    private CarModelService carModelService;

    @Mock
    private Errors errors;

    @Mock
    private CarModel form;

    @Test
    public void testValidateWhenNameIsEmpty() throws Exception {
        errors.rejectValue("name", "name.empty", null, "validation.error.empty");
        expectLastCall();
        expect(errors.getFieldValue("name")).andReturn("");
        expect(errors.getFieldValue("brand")).andReturn("brand");
        expect(form.getBrand()).andReturn(null);
        expect(form.getName()).andReturn("");
        expect(carModelService.findByName("")).andReturn(null);

        replay(form, errors, carModelService);
        carModelValidator.validate(form, errors);
        verify(form, errors, carModelService);
    }

    @Test
    public void testValidateWhenBrandIsEmpty() throws Exception {
        errors.rejectValue("brand", "brand.empty", null, "validation.error.empty");
        expectLastCall();
        expect(errors.getFieldValue("name")).andReturn("name");
        expect(errors.getFieldValue("brand")).andReturn("");
        expect(form.getBrand()).andReturn(null);
        expect(form.getName()).andReturn("");
        expect(carModelService.findByName("")).andReturn(null);

        replay(form, errors, carModelService);
        carModelValidator.validate(form, errors);
        verify(form, errors, carModelService);
    }

    @Test
    public void testValidateWhenBrandNotExist() throws Exception {
        expect(errors.getFieldValue("name")).andReturn("name");
        expect(errors.getFieldValue("brand")).andReturn("brand");
        CarBrand carBrand = new CarBrand();
        carBrand.setId(1L);
        expect(carBrandService.findById(1L)).andThrow(new ResourceNotFoundException());
        expect(form.getBrand()).andReturn(carBrand).times(2);
        errors.rejectValue("brand", "brand.not_exist", null, "validation.error.brand_not_exist");
        expectLastCall();
        expect(form.getName()).andReturn("");
        expect(carModelService.findByName("")).andReturn(null);

        replay(form, errors, carModelService, carBrandService);
        carModelValidator.validate(form, errors);
        verify(form, errors, carModelService, carBrandService);
    }

    @Test
    public void testValidateWhenNameIsDuplicated() throws Exception {
        expect(errors.getFieldValue("name")).andReturn("name");
        expect(errors.getFieldValue("brand")).andReturn("brand");
        CarBrand carBrand = new CarBrand();
        carBrand.setId(1L);
        CarModel modelWithSameName = new CarModel();
        modelWithSameName.setBrand(carBrand);
        expect(carBrandService.findById(1L)).andReturn(null);
        expect(form.getName()).andReturn("name");
        expect(carModelService.findByName("name")).andReturn(modelWithSameName);
        expect(form.getBrand()).andReturn(carBrand).times(4);
        errors.rejectValue("name", "brand.duplicated", null, "validation.error.model_duplicated");
        expectLastCall();

        replay(form, errors, carModelService, carBrandService);
        carModelValidator.validate(form, errors);
        verify(errors);
    }
}