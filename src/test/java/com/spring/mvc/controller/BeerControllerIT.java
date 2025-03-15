package com.spring.mvc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.mvc.entities.Beer;
import com.spring.mvc.mappers.BeerMapper;
import com.spring.mvc.model.BeerDTO;
import com.spring.mvc.repositories.BeerRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Rollback
@Transactional
class BeerControllerIT {
    @Autowired
    BeerController beerController;

    @Autowired
    BeerRepository beerRepository;

    @Autowired
    BeerMapper beerMapper;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    WebApplicationContext wac;

    MockMvc mockMvc;


    @BeforeEach
    void setUp() {
        // Loads Springs "Full context", meaning the entire Spring Boot application is loaded, including controllers, services, repositories, and configurations,
        // allowing tests to run with real dependencies instead of mocks.
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    void testPatchBeerBadName() throws Exception {
        Beer beer = beerRepository.findAll().get(0);

        Map<String, Object> beerMap = new HashMap<>();
        beerMap.put("beerName", "New Name 1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890");

        mockMvc.perform(patch("/api/v1/beer/" + beer.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beerMap)))
                .andExpect(status().isBadRequest());

    }

    @Test
    void testDeleteNotFound() {
        assertThrows(NotFoundException.class, () -> {
            beerController.deleteById(UUID.randomUUID());
        });
    }

    @Test
    void deleteByIdFound() {
        Beer beer = beerRepository.findAll().getFirst();

        ResponseEntity responseEntity = beerController.deleteById(beer.getId());

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

        assertThat(beerRepository.findById(beer.getId()).isEmpty());

    }

    @Test
    void testUpdateNotFound() {
        assertThrows(NotFoundException.class, () -> {
            beerController.updateById(UUID.randomUUID(), BeerDTO.builder().build());
        });
    }


    @Test
    void updateExistingBeer() {
        // Retrieve the first Beer entity from the database
        Beer beer = beerRepository.findAll().getFirst();

        // Convert the Beer entity to a BeerDTO for updating
        BeerDTO beerDTO = beerMapper.beerToBeerDto(beer);

        // Clear ID and version to prevent conflicts during update
//        beerDTO.setId(null);
//        beerDTO.setVersion(null);

        // Modify the beer name for testing the update
        final String beerName = "UPDATED";
        beerDTO.setBeerName(beerName);

        // Send an update request to the controller
        ResponseEntity responseEntity = beerController.updateById(beer.getId(), beerDTO);

        // Assert that the response status is 201 Created (indicating successful update)
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

        // Retrieve the updated Beer entity from the database
        Beer updatedBeer = beerRepository.findById(beer.getId()).get();

        // Assert that the beer name was successfully updated in the database
        assertThat(updatedBeer.getBeerName()).isEqualTo(beerName);
    }


    @Test
    void saveNewBeerTest() {
        // Create a new BeerDTO with minimal data (only beerName)
        BeerDTO beerDTO = BeerDTO.builder()
                .beerName("New Beer")
                .build();

        // Call the controller's POST handler to save the new beer
        ResponseEntity responseEntity = beerController.handlePost(beerDTO);

        // Assert that the response status is 201 Created
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));

        // Assert that the response includes a "Location" header with the URI of the new resource
        assertThat(responseEntity.getHeaders().getLocation()).isNotNull();

        // Extract the UUID from the Location header path
        String[] locationUUID = responseEntity.getHeaders().getLocation().getPath().split("/");
        UUID savedUUID = UUID.fromString(locationUUID[4]);

        // Retrieve the saved Beer entity from the repository
        Beer beer = beerRepository.findById(savedUUID).get();

        // Assert that the Beer entity was successfully saved
        assertThat(beer).isNotNull();
    }


    @Test
    void testBeerIdNotFound() {
        assertThrows(NotFoundException.class, () -> {
            beerController.getBeerById(UUID.randomUUID());
        });

    }

    @Test
    void testGetById() {
        Beer beer = beerRepository.findAll().getFirst();

        BeerDTO dto = beerController.getBeerById(beer.getId());

        assertThat(dto).isNotNull();
    }

    @Test
    void testListBeers() {
        List<BeerDTO> dtos = beerController.listBeers();
        assertThat(dtos.size()).isEqualTo(3);
    }


    @Rollback // Makes sure database changes made do not persist
    @Transactional // Wraps test in a transaction so that any operations can use rollback
    @Test
    void testEmptyList() {
        beerRepository.deleteAll();
        List<BeerDTO> dtos = beerController.listBeers();
        assertThat(dtos.size()).isEqualTo(0);

    }
}