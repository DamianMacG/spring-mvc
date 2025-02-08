package com.spring.mvc.controller;


import com.spring.mvc.model.Beer;
import com.spring.mvc.services.BeerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

import java.util.UUID;

@Slf4j
@AllArgsConstructor
@Controller
public class BeerController {

    private final BeerService beerService;

    public Beer getBeerById(UUID id) {

        log.debug("Get Beer ID in CONTROLLER was called");

       return beerService.getBeerById(id);
    }


}
