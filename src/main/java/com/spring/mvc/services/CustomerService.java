package com.spring.mvc.services;

import com.spring.mvc.model.Customer;

import java.util.List;
import java.util.UUID;

public interface CustomerService {

    List<Customer> listCustomers();

    Customer getCustomerById(UUID id);

}
