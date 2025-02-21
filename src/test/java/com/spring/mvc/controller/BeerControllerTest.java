package com.spring.mvc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.mvc.model.Beer;
import com.spring.mvc.services.BeerService;
import com.spring.mvc.services.BeerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.UUID;

import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BeerController.class)
// Sets up a test context for BeerController, including MockMvc for HTTP request testing
public class BeerControllerTest {

    @Autowired
    MockMvc mockMvc; // MockMvc allows simulating HTTP requests to test the controller in isolation

    // In tests, ObjectMapper is often used to convert objects to JSON strings before sending API requests, and to parse JSON responses into Java objects for validation.
    @Autowired
    ObjectMapper objectMapper; // Used to serialise and deserialise JSON data in tests

    @MockitoBean
    BeerService beerService; // Mocks the BeerService dependency to avoid real service calls

    BeerServiceImpl beerServiceImpl; // Creates a real instance to retrieve test data

    @BeforeEach
    void setUp() {
        beerServiceImpl = new BeerServiceImpl();
    }

    @Test
    void testUpdateBeer() throws Exception {
        Beer beer = beerServiceImpl.listBeers().get(0);

        mockMvc.perform(put("/api/v1/beer/" + beer.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beer)))
                .andExpect(status().isNoContent()); // Expecting HTTP 204 No Content - matching the controller's response

        // Verify that beerService.updateBeerById() was called exactly once with any UUID and any Beer object
        verify(beerService).updateBeerById(any(UUID.class), any(Beer.class));

    }

    @Test
    void testCreateNewBeer() throws Exception {
        // 1. Mimicking a new beer that hasn’t been assigned an ID or version yet
        Beer beer = beerServiceImpl.listBeers().getFirst();
        beer.setVersion(null);
        beer.setId(null);

        // 2. Mocking service behavior: When saveNewBeer() is called with ANY beer, return the LAST beer in the list
        given(beerService.saveNewBeer(any(Beer.class))).willReturn(beerServiceImpl.listBeers().getLast());

        // 3. Sending a POST request with the beer object as JSON
        mockMvc.perform(post("/api/v1/beer")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beer))) // <---- BEER is serialized here
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }

    // Alternate way for above test that is more explicit
//    @Test
//    void testCreateNewBeer() throws Exception {
//        // Arrange: Create a new beer (without ID or version) to mimic an API request
//        Beer newBeer = Beer.builder()
//                .beerName("Test Beer")
//                .beerStyle(Beerstyle.IPA)
//                .price(BigDecimal.valueOf(9.99))
//                .quantityOnHand(100)
//                .upc("98765")
//                .build();
//
//        // Arrange: Simulate the beer that will be returned after saving (with ID & version assigned)
//        Beer savedBeer = Beer.builder()
//                .id(UUID.randomUUID()) // Assigned by the system
//                .version(1) // First version after save
//                .beerName(newBeer.getBeerName())
//                .beerStyle(newBeer.getBeerStyle())
//                .price(newBeer.getPrice())
//                .quantityOnHand(newBeer.getQuantityOnHand())
//                .upc(newBeer.getUpc())
//                .createdDate(LocalDateTime.now())
//                .updateDate(LocalDateTime.now())
//                .build();
//
//        // Mock: When saveNewBeer() is called with any Beer, return the savedBeer object
//        given(beerService.saveNewBeer(any(Beer.class))).willReturn(savedBeer);
//
//        // Act & Assert: Perform the request and verify the response
//        mockMvc.perform(post("/api/v1/beer")
//                        .accept(MediaType.APPLICATION_JSON)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(newBeer))) // Correctly sending newBeer
//                .andExpect(status().isCreated()) // Expect HTTP 201 Created
//                .andExpect(header().exists("Location")) // Expect a Location header
//                .andExpect(jsonPath("$.id").value(savedBeer.getId().toString())) // Verify ID is returned
//                .andExpect(jsonPath("$.version").value(savedBeer.getVersion())) // Verify version is set
//                .andExpect(jsonPath("$.beerName").value(savedBeer.getBeerName())) // Verify beer name
//                .andExpect(jsonPath("$.beerStyle").value(savedBeer.getBeerStyle().toString())) // Verify style
//                .andExpect(jsonPath("$.price").value(savedBeer.getPrice().doubleValue())) // Verify price
//                .andExpect(jsonPath("$.quantityOnHand").value(savedBeer.getQuantityOnHand())); // Verify quantity
//    }


    @Test
    void getBeers() throws Exception {

        // Mock the beerService to return test data
        given(beerService.listBeers()).willReturn(beerServiceImpl.listBeers());

        // Perform the request and capture the result if you want to log it
        MvcResult result = mockMvc.perform(get("/api/v1/beer")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(3)))
                .andReturn(); // Capture the result

        // Print the JSON response to the console
        System.out.println("Response JSON: " + result.getResponse().getContentAsString());
    }


    @Test
    void getBeerById() throws Exception {

        // Retrieve a sample beer object from the service's test data
        Beer testBeer = beerServiceImpl.listBeers().get(0);

        // Configure Mockito to return the test beer when getBeerById() is called with any UUID
        given(beerService.getBeerById(testBeer.getId())).willReturn(testBeer);

        // Perform a GET request to the API endpoint, simulating a client request
        mockMvc.perform(get("/api/v1/beer/" + testBeer.getId()) // Simulates requesting a beer by ID
                        .accept(MediaType.APPLICATION_JSON)) // Requests JSON response
                .andExpect(status().isOk()) // Verifies that the response status is 200 OK
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)) // Ensures response content type is JSON
                .andExpect(jsonPath("$.id", is(testBeer.getId().toString()))) // Verify that the JSON response contains the correct beer ID
                .andExpect(jsonPath("$.beerName", is(testBeer.getBeerName()))); // Verify that the JSON response contains the correct beer name
    }
}
