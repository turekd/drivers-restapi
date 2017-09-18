package it.dturek.github.drivers.controller;

import it.dturek.github.drivers.domain.CarBrand;
import it.dturek.github.drivers.repository.CarBrandRepository;
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
public class CarBrandControllerTest extends AbstractControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private CarBrandRepository carBrandRepository;

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    @Before
    public void setUp() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
        carBrandRepository.deleteAll();
    }


    @Test
    public void testFindByIdThrows404WhenEntityNotExist() throws Exception {
        mockMvc
                .perform(get("/car_brand/0"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testFindByIdReturns200() throws Exception {
        CarBrand carBrand = new CarBrand();
        carBrand.setName("test");
        carBrandRepository.save(carBrand);

        mockMvc
                .perform(get("/car_brand/" + carBrand.getId()))
                .andExpect(jsonPath("$.id", is(carBrand.getId().intValue())))
                .andExpect(jsonPath("$.name", is(carBrand.getName())))
                .andExpect(status().isOk());

    }

    @Test
    public void testCreateWithEmptyNameShouldReturnBadRequest() throws Exception {
        mockMvc
                .perform(post("/car_brand")
                        .contentType(contentType)
                        .content(json(new CarBrand())))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateWithNotUniqueNameShouldReturnBadRequest() throws Exception {
        String name = "not_unique";
        carBrandRepository.save(new CarBrand(name));

        mockMvc
                .perform(post("/car_brand")
                        .contentType(contentType)
                        .content(json(new CarBrand(name))))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateReturns200() throws Exception {
        CarBrand carBrand = new CarBrand("foo");
        CarBrand lastInserted = carBrandRepository.save(new CarBrand("test"));

        mockMvc
                .perform(post("/car_brand")
                        .contentType(contentType)
                        .content(json(carBrand)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(lastInserted.getId().intValue() + 1)))
                .andExpect(jsonPath("$.name", is(carBrand.getName())));
    }

    @Test
    public void testFindAll() throws Exception {
        List<CarBrand> carBrands = new ArrayList<>();
        int toAdd = 3;
        for (int i = 0; i < toAdd; i++) {
            CarBrand carBrand = new CarBrand("test-" + i);
            carBrands.add(carBrand);
            carBrandRepository.save(carBrand);
        }

        mockMvc
                .perform(get("/car_brand"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(toAdd)))
                .andExpect(jsonPath("$[0].id", is(carBrands.get(0).getId().intValue())))
                .andExpect(jsonPath("$[0].name", is(carBrands.get(0).getName())))
                .andExpect(jsonPath("$[1].id", is(carBrands.get(1).getId().intValue())))
                .andExpect(jsonPath("$[1].name", is(carBrands.get(1).getName())))
                .andExpect(jsonPath("$[2].id", is(carBrands.get(2).getId().intValue())))
                .andExpect(jsonPath("$[2].name", is(carBrands.get(2).getName())))
        ;
    }

    @Test
    public void testUpdateWithNotUniqueName() throws Exception {
        CarBrand carBrand = new CarBrand();
        carBrand.setName("oldName");
        carBrandRepository.save(carBrand);
        carBrandRepository.save(new CarBrand("existingName"));

        mockMvc
                .perform(put("/car_brand/" + carBrand.getId())
                        .contentType(contentType)
                        .content(json(new CarBrand("existingName"))))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateWithEmptyName() throws Exception {
        CarBrand carBrand = new CarBrand();
        carBrand.setName("oldName");
        carBrandRepository.save(carBrand);

        mockMvc
                .perform(put("/car_brand/" + carBrand.getId())
                        .contentType(contentType)
                        .content(json(new CarBrand(""))))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateShouldReturn200() throws Exception {
        CarBrand carBrand = new CarBrand("oldName");
        carBrandRepository.save(carBrand);

        mockMvc
                .perform(put("/car_brand/" + carBrand.getId())
                        .contentType(contentType)
                        .content(json(new CarBrand("newName"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(carBrand.getId().intValue())))
                .andExpect(jsonPath("$.name", is("newName")));

        assertEquals(carBrand, carBrandRepository.findOne(carBrand.getId()));
    }

    @Test
    public void testDeleteThrows404WhenEntityNotExist() throws Exception {
        mockMvc
                .perform(delete("/car_brand/0"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteReturns200() throws Exception {
        CarBrand carBrand = new CarBrand("oldName");
        carBrandRepository.save(carBrand);

        mockMvc
                .perform(delete("/car_brand/" + carBrand.getId()))
                .andExpect(status().isOk());

        assertEquals(null, carBrandRepository.findOne(carBrand.getId()));
    }

    @Test
    public void testDeleteAllReturns200() throws Exception {
        mockMvc
                .perform(delete("/car_brand"))
                .andExpect(status().isOk());

        assertEquals(0, carBrandRepository.count());
    }

}