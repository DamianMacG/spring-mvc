package com.spring.mvc.bootstrap;

import com.spring.mvc.entities.Beer;
import com.spring.mvc.entities.Customer;
import com.spring.mvc.model.Beerstyle;
import com.spring.mvc.repositories.BeerRepository;
import com.spring.mvc.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;

@Component // Marks this class as a Spring-managed component, allowing it to be automatically detected and instantiated

@RequiredArgsConstructor
// Generates a constructor with required arguments for all final fields, ensuring dependency injection is handled automatically

public class BootstrapData implements CommandLineRunner { // Implements CommandLineRunner, which runs this class when the Spring Boot application starts

    private final BeerRepository beerRepository; // Injects BeerRepository dependency for database operations related to Beer

    private final CustomerRepository customerRepository; // Injects CustomerRepository dependency for database operations related to Customers

    @Override
    public void run(String... args) throws Exception {

        loadBeerData(); // Calls method below to load initial beer data into the database

        loadCustomerData(); // Calls method below to load initial customer data into the database
    }

    private void loadBeerData() {
        if (beerRepository.count() == 0) {

            Beer beer1 = Beer.builder()
                    .beerName("Galaxy Cat")
                    .beerStyle(Beerstyle.PALE_ALE)
                    .upc("12356")
                    .price(new BigDecimal("12.99"))
                    .quantityOnHand(122)
                    .createdDate(LocalDateTime.now())
                    .updateDate(LocalDateTime.now())
                    .build();

            Beer beer2 = Beer.builder()
                    .beerName("Crank")
                    .beerStyle(Beerstyle.PALE_ALE)
                    .upc("12356222")
                    .price(new BigDecimal("11.99"))
                    .quantityOnHand(392)
                    .createdDate(LocalDateTime.now())
                    .updateDate(LocalDateTime.now())
                    .build();

            Beer beer3 = Beer.builder()
                    .beerName("Sunshine City")
                    .beerStyle(Beerstyle.IPA)
                    .upc("12356")
                    .price(new BigDecimal("13.99"))
                    .quantityOnHand(144)
                    .createdDate(LocalDateTime.now())
                    .updateDate(LocalDateTime.now())
                    .build();

            beerRepository.save(beer1);
            beerRepository.save(beer2);
            beerRepository.save(beer3);
        }
    }

    private void loadCustomerData() {

        if (customerRepository.count() == 0) {
            Customer customer1 = Customer.builder()
                    .name("Barry")
                    .createdDate(LocalDateTime.now())
                    .lastModifiedDate(LocalDateTime.now())
                    .build();

            Customer customer2 = Customer.builder()
                    .name("Thomas")
                    .createdDate(LocalDateTime.now())
                    .lastModifiedDate(LocalDateTime.now())
                    .build();

            Customer customer3 = Customer.builder()
                    .name("Shakira")
                    .createdDate(LocalDateTime.now())
                    .lastModifiedDate(LocalDateTime.now())
                    .build();

            customerRepository.saveAll(Arrays.asList(customer1, customer2, customer3));
//        customerRepository.save(customer1);
//        customerRepository.save(customer2);
//        customerRepository.save(customer3);

        }
    }

}
