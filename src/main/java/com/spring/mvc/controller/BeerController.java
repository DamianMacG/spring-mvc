package com.spring.mvc.controller;


import com.spring.mvc.model.Beer;
import com.spring.mvc.services.BeerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@Slf4j
@AllArgsConstructor
@RestController
public class BeerController {

    private final BeerService beerService;

    @RequestMapping("/api/v1/beer")
    public List<Beer> listBeers() {
        return  beerService.listBeers();
    }

    public Beer getBeerById(UUID id) {

        log.debug("Get Beer ID in CONTROLLER was called");

       return beerService.getBeerById(id);
    }


}
