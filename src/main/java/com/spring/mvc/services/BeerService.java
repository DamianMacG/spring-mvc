package com.spring.mvc.services;

import com.spring.mvc.model.Beer;

import java.util.UUID;

public interface BeerService {

    Beer getBeerById(UUID is);
}
