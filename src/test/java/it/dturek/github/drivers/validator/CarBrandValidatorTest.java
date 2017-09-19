package it.dturek.github.drivers.validator;

import it.dturek.github.drivers.domain.CarBrand;
import it.dturek.github.drivers.service.CarBrandService;
import org.easymock.EasyMockRunner;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.validation.Errors;

import static org.easymock.EasyMock.*;

@RunWith(EasyMockRunner.class)
public class CarBrandValidatorTest {

    @TestSubject
    private CarBrandValidator carBrandValidator = new CarBrandValidator();

    @Mock
    private CarBrandService carBrandService;

    @Mock
    private Errors errors;

    @Mock
    private CarBrand carBrand;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testValidateWhenEmptyName() throws Exception {
        errors.rejectValue("name", "name.empty", null, "validation.error.empty");
        expectLastCall();
        expect(errors.getFieldValue("name")).andReturn("");
        replay(errors);
        carBrandValidator.validate(new CarBrand(), errors);
        verify(errors);
    }

    @Test
    public void testValidateWhenNameIsDuplicated() throws Exception {
        expect(carBrand.getName()).andReturn("test");
        expect(errors.getFieldValue("name")).andReturn("test");
        expect(carBrandService.findByName("test")).andReturn(new CarBrand());
        errors.rejectValue("name", "name.not_unique", null, "validation.error.not_unique");
        expectLastCall();
        replay(carBrand, errors, carBrandService);
        carBrandValidator.validate(carBrand, errors);
        verify(carBrand, errors, carBrandService);
    }
}