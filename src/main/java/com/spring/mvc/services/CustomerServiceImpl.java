package com.spring.mvc.services;

import com.spring.mvc.model.CustomerDTO;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class CustomerServiceImpl implements CustomerService {

    private Map<UUID, CustomerDTO> customerMap;

    public CustomerServiceImpl() {
        this.customerMap = new HashMap<>();


    }

    @Override
    public List<CustomerDTO> listCustomers() {
        return new ArrayList<>(customerMap.values());
    }

    @Override
    public Optional<CustomerDTO> getCustomerById(UUID id) {
        return Optional.ofNullable(customerMap.get(id));
    }

    @Override
    public CustomerDTO saveNewCustomer(CustomerDTO customer) {

        CustomerDTO savedCustomer = CustomerDTO.builder()
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
    public void updateCustomerById(UUID customerId, CustomerDTO customer) {
        CustomerDTO existing = customerMap.get(customerId);

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
    public void patchCustomerById(UUID customerId, CustomerDTO customer) {
        CustomerDTO existing = customerMap.get(customerId);

        if (StringUtils.hasText(customer.getName())) {
            existing.setName(customer.getName());
        }
    }
}
