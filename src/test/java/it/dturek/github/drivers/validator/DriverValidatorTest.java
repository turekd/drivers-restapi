package it.dturek.github.drivers.validator;

import it.dturek.github.drivers.domain.Driver;
import it.dturek.github.drivers.service.DriverService;
import org.easymock.EasyMockRunner;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.validation.Errors;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

@RunWith(EasyMockRunner.class)
public class DriverValidatorTest {

    @TestSubject
    private DriverValidator driverValidator = new DriverValidator();

    @Mock
    private DriverService driverService;

    @Mock
    private Driver form;

    @Mock
    private Errors errors;

    @Test
    public void testValidateWhenFirstnameIsEmpty() throws Exception {
        expect(errors.getFieldValue("firstName")).andReturn("");
        expect(errors.getFieldValue("lastName")).andReturn("lastName");
        expect(errors.getFieldValue("licenseNumber")).andReturn("licenseNumber");
        expect(errors.getFieldValue("dateOfBirth")).andReturn("dateOfBirth");
        errors.rejectValue("firstName", "firstName.empty", null, "validation.error.empty");
        expectLastCall();

        expect(driverService.findByLicenseNumber(null)).andReturn(null);

        replay(errors, driverService);
        driverValidator.validate(form, errors);
        verify(errors, driverService);
    }

    @Test
    public void testValidateWhenLastnameIsEmpty() throws Exception {
        expect(errors.getFieldValue("firstName")).andReturn("firstName");
        expect(errors.getFieldValue("lastName")).andReturn("");
        expect(errors.getFieldValue("licenseNumber")).andReturn("licenseNumber");
        expect(errors.getFieldValue("dateOfBirth")).andReturn("dateOfBirth");
        errors.rejectValue("lastName", "lastName.empty", null, "validation.error.empty");
        expectLastCall();

        expect(driverService.findByLicenseNumber(null)).andReturn(null);

        replay(errors, driverService);
        driverValidator.validate(form, errors);
        verify(errors, driverService);
    }

    @Test
    public void testValidateWhenLicenseNumberIsEmpty() throws Exception {
        expect(errors.getFieldValue("firstName")).andReturn("firstName");
        expect(errors.getFieldValue("lastName")).andReturn("lastName");
        expect(errors.getFieldValue("licenseNumber")).andReturn("");
        expect(errors.getFieldValue("dateOfBirth")).andReturn("dateOfBirth");
        errors.rejectValue("licenseNumber", "licenseNumber.empty", null, "validation.error.empty");
        expectLastCall();

        expect(driverService.findByLicenseNumber(null)).andReturn(null);

        replay(errors, driverService);
        driverValidator.validate(form, errors);
        verify(errors, driverService);
    }

    @Test
    public void testValidateWhenDateOfBirthIsEmpty() throws Exception {
        expect(errors.getFieldValue("firstName")).andReturn("firstName");
        expect(errors.getFieldValue("lastName")).andReturn("lastName");
        expect(errors.getFieldValue("licenseNumber")).andReturn("licenseNumber");
        expect(errors.getFieldValue("dateOfBirth")).andReturn("");
        errors.rejectValue("dateOfBirth", "dateOfBirth.empty", null, "validation.error.empty");
        expectLastCall();

        expect(driverService.findByLicenseNumber(null)).andReturn(null);

        replay(errors, driverService);
        driverValidator.validate(form, errors);
        verify(errors, driverService);
    }

    @Test
    public void testValidationWhenLicenseNumberIsDuplicated() throws Exception {
        expect(errors.getFieldValue("firstName")).andReturn("firstName");
        expect(errors.getFieldValue("lastName")).andReturn("lastName");
        expect(errors.getFieldValue("licenseNumber")).andReturn("licenseNumber");
        expect(errors.getFieldValue("dateOfBirth")).andReturn("dateOfBirth");

        Driver driverWithSameLicenseNumber = new Driver();
        driverWithSameLicenseNumber.setId(19L);
        expect(driverService.findByLicenseNumber("18")).andReturn(driverWithSameLicenseNumber);
        expect(form.getId()).andReturn(18L).times(2);
        expect(form.getLicenseNumber()).andReturn("18");
        errors.rejectValue("licenseNumber", "licenseNumber.not_unique", null, "validation.error.not_unique");
        expectLastCall();

        replay(errors, driverService, form);
        driverValidator.validate(form, errors);
        verify(errors, driverService, form);
    }
}