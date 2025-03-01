package com.spring.mvc.repositories;

import com.spring.mvc.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BeerRepository extends JpaRepository<Customer, UUID> {


}
