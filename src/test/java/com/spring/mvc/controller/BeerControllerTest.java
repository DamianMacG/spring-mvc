package com.spring.mvc.controller;

import com.spring.mvc.model.Beer;
import com.spring.mvc.services.BeerService;
import com.spring.mvc.services.BeerServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.core.Is.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BeerController.class) // Sets up a test context for BeerController, including MockMvc for HTTP request testing
class BeerControllerTest {

    @Autowired
    MockMvc mockMvc; // MockMvc allows simulating HTTP requests to test the controller in isolation

    @MockitoBean
    BeerService beerService; // Mocks the BeerService dependency to avoid real service calls

    BeerServiceImpl beerServiceImpl = new BeerServiceImpl(); // Creates a real instance to retrieve test data

    @Test
    void testListBeers() throws Exception {

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
