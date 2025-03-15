package com.spring.mvc.repositories;

import com.spring.mvc.entities.Beer;
import com.spring.mvc.model.BeerStyle;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
class BeerRepositoryTest {

    @Autowired
    BeerRepository beerRepository;

    @Test
    void testSaveBeer() {
        Beer savedBeer = beerRepository.save(Beer.builder()
                .beerName("Damian's Beer")
                .beerStyle(BeerStyle.IPA)
                .upc("23456765432")
                .price(new BigDecimal("11.99"))
                .build());

        beerRepository.flush(); // Makes sure database validation runs immediately, preventing assertions from passing before constraints are checked.

        assertThat(savedBeer).isNotNull();
        assertThat(savedBeer.getId()).isNotNull();
    }

    @Test
    void testSaveBeerNameTooLong() {

        assertThrows(ConstraintViolationException.class, () -> {
            Beer savedBeer = beerRepository.save(Beer.builder()
                    .beerName("abababababababababababababababababababababababababa")
                    .beerStyle(BeerStyle.IPA)
                    .upc("23456765432")
                    .price(new BigDecimal("11.99"))
                    .build());


            beerRepository.flush();
        });
    }
}