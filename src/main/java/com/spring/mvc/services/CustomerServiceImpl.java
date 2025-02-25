package com.spring.mvc.services;

import com.spring.mvc.model.Customer;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class CustomerServiceImpl implements CustomerService {

    private Map<UUID, Customer> customerMap;

    public CustomerServiceImpl() {
        this.customerMap = new HashMap<>();


        Customer customer1 = Customer.builder()
                .id(UUID.randomUUID())
                .version(1)
                .name("Barry")
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        Customer customer2 = Customer.builder()
                .id(UUID.randomUUID())
                .version(1)
                .name("Thomas")
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        Customer customer3 = Customer.builder()
                .id(UUID.randomUUID())
                .version(1)
                .name("Shakira")
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        customerMap.put(customer1.getId(), customer1);
        customerMap.put(customer2.getId(), customer2);
        customerMap.put(customer3.getId(), customer3);
    }

    @Override
    public List<Customer> listCustomers() {
        return new ArrayList<>(customerMap.values());
    }

    @Override
    public Optional<Customer> getCustomerById(UUID id) {
        return Optional.ofNullable(customerMap.get(id));
    }

    @Override
    public Customer saveNewCustomer(Customer customer) {

        Customer savedCustomer = Customer.builder()
                .id(UUID.randomUUID())
                .version(1)
                .name(customer.getName())
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        customerMap.put(savedCustomer.getId(), savedCustomer);

        return savedCustomer;
    }

    @Override
    public void updateCustomerById(UUID customerId, Customer customer) {
        Customer existing = customerMap.get(customerId);

        if (existing != null) {
            // Ensure required fields NEVER change or become null
            customer.setId(existing.getId()); // Keep ID unchanged
            customer.setVersion(existing.getVersion()); // Keep version unchanged
            customer.setCreatedDate(existing.getCreatedDate()); // Keep createdDate unchanged

            // Update lastModifiedDate to NOW
            customer.setLastModifiedDate(LocalDateTime.now());

            // Allow updates for name (can be null)
            customer.setName(customer.getName());

            // Replace the old object in the map
            customerMap.put(customerId, customer);
        }

    }

    @Override
    public void deleteCustomerById(UUID customerId) {
        customerMap.remove(customerId);
    }

    @Override
    public void patchCustomerById(UUID customerId, Customer customer) {
        Customer existing = customerMap.get(customerId);

        if (StringUtils.hasText(customer.getName())) {
            existing.setName(customer.getName());
        }
    }
}
