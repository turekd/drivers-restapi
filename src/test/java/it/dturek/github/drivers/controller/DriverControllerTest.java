package it.dturek.github.drivers.controller;

import it.dturek.github.drivers.domain.Driver;
import it.dturek.github.drivers.repository.DriverRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import javax.transaction.Transactional;
import java.nio.charset.Charset;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:test.properties")
@Transactional
@Rollback
public class DriverControllerTest extends AbstractControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    @Autowired
    private DriverRepository driverRepository;

    @Before
    public void setUp() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
        driverRepository.deleteAll();
    }

    @Test
    public void testFindAll() throws Exception {
        List<Driver> drivers = new ArrayList<>();
        int size = 2;
        for (int i = 0; i < size; i++) {
            Driver driver = createDriver("firstname-" + i, "lastname-" + i, String.valueOf(i), new Date(i));
            driverRepository.save(driver);
            drivers.add(driver);
        }

        mockMvc
                .perform(get("/driver"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(size)))
                .andExpect(jsonPath("$[0].id", is(drivers.get(0).getId().intValue())))
                .andExpect(jsonPath("$[0].firstName", is(drivers.get(0).getFirstName())))
                .andExpect(jsonPath("$[0].lastName", is(drivers.get(0).getLastName())))
                .andExpect(jsonPath("$[0].licenseNumber", is(drivers.get(0).getLicenseNumber())))
                .andExpect(jsonPath("$[0].dateOfBirth", is(drivers.get(0).getDateOfBirth().toString())))
                .andExpect(jsonPath("$[1].id", is(drivers.get(1).getId().intValue())))
                .andExpect(jsonPath("$[1].firstName", is(drivers.get(1).getFirstName())))
                .andExpect(jsonPath("$[1].lastName", is(drivers.get(1).getLastName())))
                .andExpect(jsonPath("$[1].licenseNumber", is(drivers.get(1).getLicenseNumber())))
                .andExpect(jsonPath("$[1].dateOfBirth", is(drivers.get(1).getDateOfBirth().toString())));
    }

    @Test
    public void testFindByIdThrows404WhenResourceNotExist() throws Exception {
        mockMvc
                .perform(get("/driver/0"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testFindByIdReturns200WhenResourceExists() throws Exception {
        Driver driver = createDriver("firstname", "lastname", "12345", new Date(123));
        driverRepository.save(driver);

        mockMvc
                .perform(get("/driver/" + driver.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(driver.getId().intValue())))
                .andExpect(jsonPath("$.firstName", is("firstname")))
                .andExpect(jsonPath("$.lastName", is("lastname")))
                .andExpect(jsonPath("$.licenseNumber", is("12345")))
                .andExpect(jsonPath("$.dateOfBirth", is("1970-01-01")));
    }

    @Test
    public void testDeleteAll() throws Exception {
        int size = 3;
        for (int i = 0; i < size; i++) {
            Driver driver = new Driver();
            driver.setFirstName("firstname-" + i);
            driver.setLastName("lastname-" + i);
            driver.setLicenseNumber(String.valueOf(i));
            driver.setDateOfBirth(new Date(i));
            driverRepository.save(driver);
        }

        mockMvc
                .perform(delete("/driver"))
                .andExpect(status().isOk());

        assertEquals(0, driverRepository.count());
    }

    @Test
    public void testDeleteByIdReturns404WhenResourceNotExist() throws Exception {
        mockMvc
                .perform(delete("/driver/0"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteByIdReturns200ResourceExists() throws Exception {
        Driver driver = createDriver("firstname", "lastname", "12345", new Date(123));
        driverRepository.save(driver);

        mockMvc
                .perform(delete("/driver/" + driver.getId()))
                .andExpect(status().isOk());

        assertEquals(null, driverRepository.findOne(driver.getId()));
    }

    @Test
    public void testCreateReturns200WhenDataIsValid() throws Exception {
        Driver driver = createDriver("firstname", "lastname", "12345", new Date(123));

        mockMvc
                .perform(post("/driver")
                        .contentType(contentType)
                        .content(json(driver)))
                .andExpect(status().isOk());
    }

    @Test
    public void testCreateReturnsErrorWhenLicenseNumberIsDuplicated() throws Exception {
        driverRepository.save(createDriver("firstname", "lastname", "12345", new Date(123)));
        Driver driver = createDriver("firstname", "lastname", "12345", new Date(123));

        mockMvc
                .perform(post("/driver")
                        .contentType(contentType)
                        .content(json(driver)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateReturnsErrorWhenFirstNameIsEmpty() throws Exception {
        mockMvc
                .perform(post("/driver")
                        .contentType(contentType)
                        .content(json(createDriver("", "a", "1", new Date(1)))))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateReturnsErrorWhenLastNameIsEmpty() throws Exception {
        mockMvc
                .perform(post("/driver")
                        .contentType(contentType)
                        .content(json(createDriver("a", "", "1", new Date(1)))))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateReturnsErrorWhenLicenseNumberIsEmpty() throws Exception {
        mockMvc
                .perform(post("/driver")
                        .contentType(contentType)
                        .content(json(createDriver("a", "b", "", new Date(1)))))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateReturnsErrorWhenDateOfBirthIsEmpty() throws Exception {
        mockMvc
                .perform(post("/driver")
                        .contentType(contentType)
                        .content(json(createDriver("a", "a", "1", null))))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateReturns200WhenDataIsValid() throws Exception {
        Driver insertedDriver = driverRepository.save(createDriver("firstname", "lastname", "12345", new Date(123)));
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 2000);
        cal.set(Calendar.MONTH, Calendar.JANUARY);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        Driver driver = createDriver("firstname2", "lastname2", "123456", new Date(cal.getTimeInMillis()));

        mockMvc
                .perform(put("/driver/" + insertedDriver.getId())
                        .contentType(contentType)
                        .content(json(driver)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(insertedDriver.getId().intValue())))
                .andExpect(jsonPath("$.firstName", is("firstname2")))
                .andExpect(jsonPath("$.lastName", is("lastname2")))
                .andExpect(jsonPath("$.licenseNumber", is("123456")))
                .andExpect(jsonPath("$.dateOfBirth", is("2000-01-01")));
    }

    private Driver createDriver(String firstName, String lastName, String licenseNumber, Date dateOfBirth) {
        Driver driver = new Driver();
        driver.setFirstName(firstName);
        driver.setLastName(lastName);
        driver.setLicenseNumber(licenseNumber);
        driver.setDateOfBirth(dateOfBirth);
        return driver;
    }
}