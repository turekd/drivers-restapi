package it.dturek.github.drivers.controller;

import it.dturek.github.drivers.domain.CarBrand;
import it.dturek.github.drivers.domain.CarModel;
import it.dturek.github.drivers.domain.Driver;
import it.dturek.github.drivers.domain.DriverCar;
import it.dturek.github.drivers.repository.CarBrandRepository;
import it.dturek.github.drivers.repository.CarModelRepository;
import it.dturek.github.drivers.repository.DriverCarRepository;
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
import java.util.logging.Logger;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:test.properties")
@Transactional
@Rollback
public class DriverCarControllerTest extends AbstractControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private CarBrandRepository carBrandRepository;

    @Autowired
    private CarModelRepository carModelRepository;

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private DriverCarRepository driverCarRepository;

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    @Before
    public void setUp() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testFindAll() throws Exception {
        DriverCar driverCar = createDriverCar(createCarModel("BMW", "760li"), createDriver("John", "Example",
                "987123", getDate(1980, Calendar.JANUARY, 1)),
                getDate(2010, Calendar.MARCH, 1), getDate(2010, Calendar.MARCH, 30));

        mockMvc
                .perform(get("/driver_car"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(driverCar.getId().intValue())))
                .andExpect(jsonPath("$[0].carModel.id", is(driverCar.getCarModel().getId().intValue())))
                .andExpect(jsonPath("$[0].carModel.name", is(driverCar.getCarModel().getName())))
                .andExpect(jsonPath("$[0].carModel.brand.id", is(driverCar.getCarModel().getBrand().getId().intValue())))
                .andExpect(jsonPath("$[0].carModel.brand.name", is(driverCar.getCarModel().getBrand().getName())))
                .andExpect(jsonPath("$[0].driver.id", is(driverCar.getDriver().getId().intValue())))
                .andExpect(jsonPath("$[0].driver.firstName", is(driverCar.getDriver().getFirstName())))
                .andExpect(jsonPath("$[0].driver.lastName", is(driverCar.getDriver().getLastName())))
                .andExpect(jsonPath("$[0].driver.licenseNumber", is(driverCar.getDriver().getLicenseNumber())))
                .andExpect(jsonPath("$[0].driver.dateOfBirth", is(driverCar.getDriver().getDateOfBirth().toString())))
                .andExpect(jsonPath("$[0].dateFrom", is(driverCar.getDateFrom().toString())))
                .andExpect(jsonPath("$[0].dateTo", is(driverCar.getDateTo().toString())));
    }

    @Test
    public void testFindByIdReturns404WhenResourceNotExist() throws Exception {
        mockMvc
                .perform(get("/driver_car/0"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testFindByIdReturns200WhenResourceExists() throws Exception {
        DriverCar driverCar = createDriverCar(createCarModel("BMW", "760li"), createDriver("John", "Example",
                "987123", getDate(1980, Calendar.JANUARY, 1)),
                getDate(2010, Calendar.MARCH, 1), getDate(2010, Calendar.MARCH, 30));

        mockMvc
                .perform(get("/driver_car/" + driverCar.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(driverCar.getId().intValue())))
                .andExpect(jsonPath("$.carModel.id", is(driverCar.getCarModel().getId().intValue())))
                .andExpect(jsonPath("$.carModel.name", is(driverCar.getCarModel().getName())))
                .andExpect(jsonPath("$.carModel.brand.id", is(driverCar.getCarModel().getBrand().getId().intValue())))
                .andExpect(jsonPath("$.carModel.brand.name", is(driverCar.getCarModel().getBrand().getName())))
                .andExpect(jsonPath("$.driver.id", is(driverCar.getDriver().getId().intValue())))
                .andExpect(jsonPath("$.driver.firstName", is(driverCar.getDriver().getFirstName())))
                .andExpect(jsonPath("$.driver.lastName", is(driverCar.getDriver().getLastName())))
                .andExpect(jsonPath("$.driver.licenseNumber", is(driverCar.getDriver().getLicenseNumber())))
                .andExpect(jsonPath("$.driver.dateOfBirth", is(driverCar.getDriver().getDateOfBirth().toString())))
                .andExpect(jsonPath("$.dateFrom", is(driverCar.getDateFrom().toString())))
                .andExpect(jsonPath("$.dateTo", is(driverCar.getDateTo().toString())));
    }

    @Test
    public void testCreateReturnsErrorWhenCarModelIsEmpty() throws Exception {
        DriverCar driverCar = getDriverCar(null, createDriver("A", "B", "123",
                getDate(1990, 1, 1)), getDate(2000, 1, 1), getDate(2010, 1, 1));

        mockMvc
                .perform(post("/driver_car")
                        .contentType(contentType)
                        .content(json(driverCar)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateReturns404WhenResourceNotExist() throws Exception {
        DriverCar driverCar = getDriverCar(createCarModel("a", "b"), createDriver("A", "B", "123",
                getDate(1990, 1, 1)), getDate(2000, 1, 1), getDate(2010, 1, 1));

        mockMvc
                .perform(put("/driver_car/0")
                        .contentType(contentType)
                        .content(json(driverCar)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdateReturnsErrorWhenCarModelIsEmpty() throws Exception {
        DriverCar driverCar = getDriverCar(null, createDriver("A", "B", "123",
                getDate(1990, 1, 1)), getDate(2000, 1, 1), getDate(2010, 1, 1));

        mockMvc
                .perform(put("/driver_car/0")
                        .contentType(contentType)
                        .content(json(driverCar)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateReturnsErrorWhenDriverIsEmpty() throws Exception {
        DriverCar driverCar = getDriverCar(createCarModel("brand", "model"), null,
                getDate(2000, 1, 1), getDate(2010, 1, 1));

        mockMvc
                .perform(post("/driver_car")
                        .contentType(contentType)
                        .content(json(driverCar)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateReturnsErrorWhenDriverIsEmpty() throws Exception {
        DriverCar driverCar = getDriverCar(createCarModel("brand", "model"), null,
                getDate(2000, 1, 1), getDate(2010, 1, 1));

        mockMvc
                .perform(put("/driver_car/0")
                        .contentType(contentType)
                        .content(json(driverCar)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateReturnsErrorWhenDateFromIsEmpty() throws Exception {
        DriverCar driverCar = getDriverCar(createCarModel("brand", "model"),
                createDriver("A", "B", "123",
                        getDate(1990, 1, 1)), null, getDate(2010, 1, 1));

        mockMvc
                .perform(post("/driver_car")
                        .contentType(contentType)
                        .content(json(driverCar)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateReturnsErrorWhenDateFromIsEmpty() throws Exception {
        DriverCar driverCar = getDriverCar(createCarModel("brand", "model"),
                createDriver("A", "B", "123",
                        getDate(1990, 1, 1)), null, getDate(2010, 1, 1));

        mockMvc
                .perform(put("/driver_car/0")
                        .contentType(contentType)
                        .content(json(driverCar)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateReturnsErrorWhenDateToIsEmpty() throws Exception {
        DriverCar driverCar = getDriverCar(createCarModel("brand", "model"),
                createDriver("A", "B", "123",
                        getDate(1990, 1, 1)), getDate(2010, 1, 1), null);

        mockMvc
                .perform(post("/driver_car")
                        .contentType(contentType)
                        .content(json(driverCar)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateReturnsErrorWhenDateToIsEmpty() throws Exception {
        DriverCar driverCar = getDriverCar(createCarModel("brand", "model"),
                createDriver("A", "B", "123",
                        getDate(1990, 1, 1)), getDate(2010, 1, 1), null);

        mockMvc
                .perform(put("/driver_car/0")
                        .contentType(contentType)
                        .content(json(driverCar)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateReturnsErrorWhenDateToIsLowerThanDateFrom() throws Exception {
        DriverCar driverCar = getDriverCar(createCarModel("brand", "model"),
                createDriver("A", "B", "123",
                        getDate(1990, 1, 1)), getDate(2010, 1, 1), getDate(2000, 1, 1));

        mockMvc
                .perform(post("/driver_car")
                        .contentType(contentType)
                        .content(json(driverCar)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateReturnsErrorWhenDateToIsLowerThanDateFrom() throws Exception {
        DriverCar driverCar = getDriverCar(createCarModel("brand", "model"),
                createDriver("A", "B", "123",
                        getDate(1990, 1, 1)), getDate(2010, 1, 1), getDate(2000, 1, 1));

        mockMvc
                .perform(put("/driver_car/0")
                        .contentType(contentType)
                        .content(json(driverCar)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateReturnsErrorWhenDriverNotExists() throws Exception {
        DriverCar driverCar = getDriverCar(createCarModel("brand", "model"),
                createDriver("A", "B", "123",
                        getDate(1990, 1, 1)), getDate(2000, 1, 1), getDate(2010, 1, 1));
        driverCar.getDriver().setId(0L);

        mockMvc
                .perform(post("/driver_car")
                        .contentType(contentType)
                        .content(json(driverCar)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateReturnsErrorWhenDriverNotExists() throws Exception {
        DriverCar driverCar = getDriverCar(createCarModel("brand", "model"),
                createDriver("A", "B", "123",
                        getDate(1990, 1, 1)), getDate(2000, 1, 1), getDate(2010, 1, 1));
        driverCar.getDriver().setId(0L);

        mockMvc
                .perform(put("/driver_car/0")
                        .contentType(contentType)
                        .content(json(driverCar)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateReturnsErrorWhenCarModelNotExist() throws Exception {
        DriverCar driverCar = getDriverCar(createCarModel("brand", "model"),
                createDriver("A", "B", "123",
                        getDate(1990, 1, 1)), getDate(2000, 1, 1), getDate(2010, 1, 1));
        driverCar.getCarModel().setId(0L);

        mockMvc
                .perform(post("/driver_car")
                        .contentType(contentType)
                        .content(json(driverCar)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateReturnsErrorWhenCarModelNotExist() throws Exception {
        DriverCar driverCar = getDriverCar(createCarModel("brand", "model"),
                createDriver("A", "B", "123",
                        getDate(1990, 1, 1)), getDate(2000, 1, 1), getDate(2010, 1, 1));
        driverCar.getCarModel().setId(0L);

        mockMvc
                .perform(put("/driver_car/0")
                        .contentType(contentType)
                        .content(json(driverCar)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateReturns200WhenDataIsValid() throws Exception {
        DriverCar driverCar = getDriverCar(createCarModel("brand", "model"),
                createDriver("A", "B", "123",
                        getDate(1990, 1, 1)), getDate(2000, 1, 1), getDate(2010, 1, 1));

        mockMvc
                .perform(post("/driver_car")
                        .contentType(contentType)
                        .content(json(driverCar)))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdateReturns200WhenDataIsValid() throws Exception {
        DriverCar driverCar = createDriverCar(createCarModel("brand", "model"),
                createDriver("A", "B", "123",
                        getDate(1990, 1, 1)), getDate(2000, 1, 1), getDate(2010, 1, 1));

        mockMvc
                .perform(put("/driver_car/" + driverCar.getId())
                        .contentType(contentType)
                        .content(json(driverCar)))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteByIdReturns404WhenResourceNotExist() throws Exception {
        mockMvc
                .perform(delete("/driver_car/0"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteByIdReturns200() throws Exception {
        DriverCar driverCar = createDriverCar(createCarModel("brand", "model"),
                createDriver("A", "B", "123",
                        getDate(1990, 1, 1)), getDate(2000, 1, 1), getDate(2010, 1, 1));

        mockMvc
                .perform(delete("/driver_car/" + driverCar.getId()))
                .andExpect(status().isOk());

        assertEquals(null, driverCarRepository.findOne(driverCar.getId()));
    }

    @Test
    public void testDeleteAll() throws Exception {
        for (int i = 0; i < 5; i++) {
            driverCarRepository.save(createDriverCar(createCarModel("brand" + i, "car" + i),
                    createDriver("John", "Example",
                            "987123" + i, getDate(1980, Calendar.JANUARY, 1)),
                    getDate(2010, Calendar.MARCH, 1), getDate(2010, Calendar.MARCH, 30)));
        }

        mockMvc
                .perform(delete("/driver_car"))
                .andExpect(status().isOk());

        assertEquals(0, driverCarRepository.count());
    }


    private Driver createDriver(String driverFirstName, String driverLastName, String driverLicenseNumber, Date driverDateOfBirth) {
        Driver driver = new Driver();
        driver.setFirstName(driverFirstName);
        driver.setLastName(driverLastName);
        driver.setLicenseNumber(driverLicenseNumber);
        driver.setDateOfBirth(driverDateOfBirth);
        driverRepository.save(driver);
        return driver;
    }

    private CarModel createCarModel(String carBrandName, String carModelName) {
        CarBrand carBrand = new CarBrand(carBrandName);
        carBrandRepository.save(carBrand);

        CarModel carModel = new CarModel(carBrand, carModelName);
        carModelRepository.save(carModel);
        return carModel;
    }

    private DriverCar createDriverCar(CarModel carModel, Driver driver, Date driverCarDateFrom, Date driverCarDateTo) {
        DriverCar driverCar = getDriverCar(carModel, driver, driverCarDateFrom, driverCarDateTo);
        driverCarRepository.save(driverCar);
        return driverCar;
    }

    private DriverCar getDriverCar(CarModel carModel, Driver driver, Date driverCarDateFrom, Date driverCarDateTo) {
        DriverCar driverCar = new DriverCar();
        driverCar.setCarModel(carModel);
        driverCar.setDriver(driver);
        driverCar.setDateFrom(driverCarDateFrom);
        driverCar.setDateTo(driverCarDateTo);
        return driverCar;
    }

    private Date getDate(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        return new Date(cal.getTimeInMillis());
    }
}