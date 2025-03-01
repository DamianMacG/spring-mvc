package com.spring.mvc.repositories;

import com.spring.mvc.entities.Beer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CustomerRepository extends JpaRepository<Beer, UUID> {


}
