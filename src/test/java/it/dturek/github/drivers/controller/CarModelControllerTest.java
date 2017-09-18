package it.dturek.github.drivers.controller;

import it.dturek.github.drivers.domain.CarBrand;
import it.dturek.github.drivers.domain.CarModel;
import it.dturek.github.drivers.repository.CarBrandRepository;
import it.dturek.github.drivers.repository.CarModelRepository;
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
import java.util.ArrayList;
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
public class CarModelControllerTest extends AbstractControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    @Autowired
    private CarModelRepository carModelRepository;

    @Autowired
    private CarBrandRepository carBrandRepository;

    @Before
    public void setUp() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
        carModelRepository.deleteAll();
        carBrandRepository.deleteAll();
    }

    @Test
    public void testCreateWhenBrandNotExist() throws Exception {
        CarBrand carBrand = new CarBrand();
        carBrand.setId(1L);
        mockMvc
                .perform(post("/car_model")
                        .contentType(contentType)
                        .content(json(new CarModel(carBrand, "model"))))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateWhenBrandIsEmpty() throws Exception {
        mockMvc
                .perform(post("/car_model")
                        .contentType(contentType)
                        .content(json(new CarModel(null, "model"))))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateWhenNameAlreadyExist() throws Exception {
        CarBrand carBrand = carBrandRepository.save(new CarBrand("test brand"));
        carModelRepository.save(new CarModel(carBrand, "test"));
        mockMvc
                .perform(post("/car_model")
                        .contentType(contentType)
                        .content(json(new CarModel(carBrand, "test"))))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateWhenModelNameIsEmpty() throws Exception {
        CarBrand carBrand = carBrandRepository.save(new CarBrand("test brand"));
        mockMvc
                .perform(post("/car_model")
                        .contentType(contentType)
                        .content(json(new CarModel(carBrand, ""))))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateReturns200() throws Exception {
        CarBrand carBrand = carBrandRepository.save(new CarBrand("test brand"));
        mockMvc
                .perform(post("/car_model")
                        .contentType(contentType)
                        .content(json(new CarModel(carBrand, "test"))))
                .andExpect(status().isOk());
    }

    @Test
    public void testFindAll() throws Exception {
        List<CarModel> carModels = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            CarBrand carBrand = carBrandRepository.save(new CarBrand("brand-" + i));
            CarModel carModel = new CarModel(carBrand, "model-" + i);
            carModels.add(carModel);
            carModelRepository.save(carModel);
        }

        mockMvc
                .perform(get("/car_model"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].id", is(carModels.get(0).getId().intValue())))
                .andExpect(jsonPath("$[0].brand.id", is(carModels.get(0).getBrand().getId().intValue())))
                .andExpect(jsonPath("$[0].brand.name", is(carModels.get(0).getBrand().getName())))
                .andExpect(jsonPath("$[1].id", is(carModels.get(1).getId().intValue())))
                .andExpect(jsonPath("$[1].brand.id", is(carModels.get(1).getBrand().getId().intValue())))
                .andExpect(jsonPath("$[1].brand.name", is(carModels.get(1).getBrand().getName())))
                .andExpect(jsonPath("$[1].name", is(carModels.get(1).getName())))
                .andExpect(jsonPath("$[2].id", is(carModels.get(2).getId().intValue())))
                .andExpect(jsonPath("$[2].brand.id", is(carModels.get(2).getBrand().getId().intValue())))
                .andExpect(jsonPath("$[2].brand.name", is(carModels.get(2).getBrand().getName())))
                .andExpect(jsonPath("$[2].name", is(carModels.get(2).getName())));
    }

    @Test
    public void testUpdateWhenModelNotExist() throws Exception {
        CarBrand carBrand = carBrandRepository.save(new CarBrand("test brand"));
        CarModel carModel = new CarModel(carBrand, "test");

        mockMvc
                .perform(put("/car_model/1")
                        .contentType(contentType)
                        .content(json(carModel)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdateWhenBrandNotExist() throws Exception {
        CarBrand carBrand = carBrandRepository.save(new CarBrand("test brand"));
        CarModel carModel = carModelRepository.save(new CarModel(carBrand, "test"));
        CarBrand newBrand = new CarBrand("not existing brand");
        newBrand.setId(carBrand.getId() + 1);
        CarModel newCarModel = new CarModel(newBrand, "new name");

        mockMvc
                .perform(put("/car_model/" + carModel.getId())
                        .contentType(contentType)
                        .content(json(newCarModel)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateWhenModelNameIsNotUnique() throws Exception {
        CarBrand carBrand = carBrandRepository.save(new CarBrand("test brand"));
        carModelRepository.save(new CarModel(carBrand, "test"));
        CarModel carModel = new CarModel(carBrand, "test");

        mockMvc
                .perform(put("/car_model/" + carModel.getId())
                        .contentType(contentType)
                        .content(json(carModel)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateReturns200() throws Exception {
        CarBrand carBrand = carBrandRepository.save(new CarBrand("test brand"));
        CarModel insertedModel = carModelRepository.save(new CarModel(carBrand, "test"));
        CarModel carModel = new CarModel(carBrand, "test2");

        mockMvc
                .perform(put("/car_model/" + insertedModel.getId())
                        .contentType(contentType)
                        .content(json(carModel)))
                .andExpect(status().isOk());

        carModel.setId(insertedModel.getId());
        assertEquals(carModel, carModelRepository.findOne(insertedModel.getId()));
    }

    @Test
    public void testFindByIdThrows404WhenNotExist() throws Exception {
        mockMvc
                .perform(get("/car_model/0"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testFindByIdReturns200() throws Exception {
        CarBrand carBrand = carBrandRepository.save(new CarBrand("test brand"));
        CarModel carModel = carModelRepository.save(new CarModel(carBrand, "test"));

        mockMvc
                .perform(get("/car_model/" + carModel.getId()))
                .andExpect(jsonPath("$.id", is(carModel.getId().intValue())))
                .andExpect(jsonPath("$.name", is(carModel.getName())))
                .andExpect(jsonPath("$.brand.id", is(carModel.getBrand().getId().intValue())))
                .andExpect(jsonPath("$.brand.name", is(carModel.getBrand().getName())))
                .andExpect(status().isOk());

        assertEquals(carModel, carModelRepository.findOne(carModel.getId()));
    }

    @Test
    public void testDeleteWhenNotExist() throws Exception {
        mockMvc
                .perform(delete("/car_model/0"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteReturns200() throws Exception {
        CarBrand carBrand = carBrandRepository.save(new CarBrand("test brand"));
        CarModel carModel = carModelRepository.save(new CarModel(carBrand, "test"));

        mockMvc
                .perform(delete("/car_model/" + carModel.getId()))
                .andExpect(status().isOk());

        assertEquals(null, carModelRepository.findOne(carModel.getId()));
    }

    @Test
    public void testDeleteAll() throws Exception {
        mockMvc
                .perform(delete("/car_model"))
                .andExpect(status().isOk());

        assertEquals(0, carModelRepository.count());
    }
}