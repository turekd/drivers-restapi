package it.dturek.github.drivers.validator;

import it.dturek.github.drivers.domain.CarModel;
import it.dturek.github.drivers.domain.Driver;
import it.dturek.github.drivers.domain.DriverCar;
import it.dturek.github.drivers.exception.ResourceNotFoundException;
import it.dturek.github.drivers.service.CarModelService;
import it.dturek.github.drivers.service.DriverService;
import org.easymock.EasyMockRunner;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.validation.Errors;

import java.sql.Date;
import java.util.Calendar;

import static org.easymock.EasyMock.*;

@RunWith(EasyMockRunner.class)
public class DriverCarValidatorTest {

    @TestSubject
    private DriverCarValidator driverCarValidator = new DriverCarValidator();

    @Mock
    private DriverService driverService;

    @Mock
    private CarModelService carModelService;

    @Mock
    private Errors errors;

    @Mock
    private DriverCar form;

    @Test
    public void testValidateWhenWhenCarModelIsEmpty() throws Exception {
        expect(errors.getFieldValue("carModel")).andReturn("");
        errors.rejectValue("carModel", "carModel.empty", null, "validation.error.empty");
        expectLastCall();

        expect(errors.getFieldValue("driver")).andReturn("driver");
        expect(errors.getFieldValue("dateFrom")).andReturn("dateFrom");
        expect(errors.getFieldValue("dateTo")).andReturn("dateTo");

        expect(form.getDateFrom()).andReturn(null);
        expect(form.getDriver()).andReturn(null);
        expect(form.getCarModel()).andReturn(null);

        replay(errors, form);
        driverCarValidator.validate(form, errors);
        verify(errors, form);
    }

    @Test
    public void testValidateWhenDriverIsEmpty() throws Exception {
        expect(errors.getFieldValue("driver")).andReturn("");
        errors.rejectValue("driver", "driver.empty", null, "validation.error.empty");
        expectLastCall();

        expect(errors.getFieldValue("carModel")).andReturn("carModel");
        expect(errors.getFieldValue("dateFrom")).andReturn("dateFrom");
        expect(errors.getFieldValue("dateTo")).andReturn("dateTo");

        expect(form.getDateFrom()).andReturn(null);
        expect(form.getDriver()).andReturn(null);
        expect(form.getCarModel()).andReturn(null);

        replay(errors, form);
        driverCarValidator.validate(form, errors);
        verify(errors, form);
    }

    @Test
    public void testValidateWhenDateFromIsEmpty() throws Exception {
        expect(errors.getFieldValue("dateFrom")).andReturn("");
        errors.rejectValue("dateFrom", "dateFrom.empty", null, "validation.error.empty");
        expectLastCall();

        expect(errors.getFieldValue("driver")).andReturn("driver");
        expect(errors.getFieldValue("carModel")).andReturn("carModel");
        expect(errors.getFieldValue("dateTo")).andReturn("dateTo");

        expect(form.getDateFrom()).andReturn(null);
        expect(form.getDriver()).andReturn(null);
        expect(form.getCarModel()).andReturn(null);

        replay(errors, form);
        driverCarValidator.validate(form, errors);
        verify(errors, form);
    }

    @Test
    public void testValidateWhenDateToIsEmpty() throws Exception {
        expect(errors.getFieldValue("dateTo")).andReturn("");
        errors.rejectValue("dateTo", "dateTo.empty", null, "validation.error.empty");
        expectLastCall();

        expect(errors.getFieldValue("driver")).andReturn("driver");
        expect(errors.getFieldValue("carModel")).andReturn("carModel");
        expect(errors.getFieldValue("dateFrom")).andReturn("dateFrom");

        expect(form.getDateFrom()).andReturn(null);
        expect(form.getDriver()).andReturn(null);
        expect(form.getCarModel()).andReturn(null);

        replay(errors, form);
        driverCarValidator.validate(form, errors);
        verify(errors, form);
    }

    @Test
    public void testValidateWhenDateToIsLowerThanDateFrom() throws Exception {
        Date dateFrom = getDate(2000, 1, 30);
        Date dateTo = getDate(2000, 1, 1);

        expect(errors.getFieldValue("driver")).andReturn("driver");
        expect(errors.getFieldValue("carModel")).andReturn("carModel");
        expect(errors.getFieldValue("dateFrom")).andReturn(dateFrom.toString());
        expect(errors.getFieldValue("dateTo")).andReturn(dateTo.toString());

        expect(form.getDateFrom()).andReturn(dateFrom).times(2);
        expect(form.getDateTo()).andReturn(dateTo).times(2);
        expect(form.getDriver()).andReturn(null);
        expect(form.getCarModel()).andReturn(null);
        errors.rejectValue("dateFrom", "dateFrom.invalid", null, "validation.error.dateto_lower_than_datefrom");
        expectLastCall();

        replay(errors, form);
        driverCarValidator.validate(form, errors);
        verify(errors, form);
    }

    @Test
    public void testValidateWhenDriverNotExist() throws Exception {
        expect(errors.getFieldValue("driver")).andReturn("driver");
        expect(errors.getFieldValue("carModel")).andReturn("carModel");
        expect(errors.getFieldValue("dateFrom")).andReturn("dateFrom");
        expect(errors.getFieldValue("dateTo")).andReturn("dateTo");

        Driver driver = new Driver();
        driver.setId(18L);
        expect(form.getDriver()).andReturn(driver).times(2);
        expect(driverService.findById(18L)).andThrow(new ResourceNotFoundException());
        errors.rejectValue("driver", "driver.not_exist", null, "validation.error.driver_not_exist");
        expectLastCall();

        expect(form.getDateFrom()).andReturn(null);
        expect(form.getCarModel()).andReturn(null);

        replay(errors, form, driverService);
        driverCarValidator.validate(form, errors);
        verify(errors, form, driverService);
    }

    @Test
    public void testValidateWhenCarModelNotExist() throws Exception {
        expect(errors.getFieldValue("driver")).andReturn("driver");
        expect(errors.getFieldValue("carModel")).andReturn("carModel");
        expect(errors.getFieldValue("dateFrom")).andReturn("dateFrom");
        expect(errors.getFieldValue("dateTo")).andReturn("dateTo");

        CarModel carModel = new CarModel();
        carModel.setId(18L);
        expect(form.getCarModel()).andReturn(carModel).times(2);
        expect(carModelService.findById(18L)).andThrow(new ResourceNotFoundException());
        errors.rejectValue("carModel", "model.not_exist", null, "validation.error.model_not_exist");
        expectLastCall();

        expect(form.getDateFrom()).andReturn(null);
        expect(form.getDriver()).andReturn(null);

        replay(errors, form, carModelService);
        driverCarValidator.validate(form, errors);
        verify(errors, form, carModelService);
    }

    private Date getDate(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        return new Date(cal.getTimeInMillis());
    }
}