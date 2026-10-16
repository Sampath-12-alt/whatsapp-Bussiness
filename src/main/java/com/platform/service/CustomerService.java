// This service applies customer rules and uses CustomerRepository for database access.
// It keeps every customer operation scoped to the business in the request.
package com.platform.service;

import com.platform.entity.Customer;
import com.platform.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

// Provides customer creation, lookup, update, and business-scoped duplicate checks.
@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    // Spring supplies the repository through constructor-based dependency injection.
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    /**
     * Creates a customer owned by the given business.
     *++
     * @param businessId ID of the business that owns the customer
     * @param name customer's name
     * @param phone customer's WhatsApp phone number
     * @param address optional customer address
     * @return the saved customer, including its generated ID and timestamps
     */
    public Customer createCustomer(
            Long businessId, String name, String phone, String address) {
        ensurePhoneIsAvailable(businessId, phone, null);

        LocalDateTime now = LocalDateTime.now();
        Customer customer = new Customer();
        customer.setBusinessId(businessId);
        customer.setName(name);
        customer.setPhone(phone);
        customer.setAddress(address);
        customer.setCreatedAt(now);
        customer.setUpdatedAt(now);

        return customerRepository.save(customer);
    }

    /**
     * Lists all customers belonging to the given business.
     *
     * @param businessId ID of the business whose customers are requested
     * @return that business's customers
     */
    public List<Customer> getCustomers(Long businessId) {
        return customerRepository.findAllByBusinessIdOrderByIdAsc(businessId);
    }

    /**
     * Finds one customer only when it belongs to the given business.
     *
     * @param businessId ID of the business that must own the customer
     * @param customerId ID of the requested customer
     * @return the matching customer
     * @throws ResponseStatusException with HTTP 404 when no matching customer exists
     */
    public Customer getCustomer(Long businessId, Long customerId) {
        return customerRepository.findByIdAndBusinessId(customerId, businessId)
                .orElseThrow(() -> new ResponseStatusException(
                        NOT_FOUND, "Customer not found"));
    }

    /**
     * Finds a customer by WhatsApp phone number within one business.
     *
     * @param businessId ID of the business whose customers are searched
     * @param phone WhatsApp phone number to find
     * @return the matching customer
     * @throws ResponseStatusException with HTTP 404 when no matching customer exists
     */
    public Customer findByPhone(Long businessId, String phone) {
        return customerRepository.findByBusinessIdAndPhone(businessId, phone)
                .orElseThrow(() -> new ResponseStatusException(
                        NOT_FOUND, "Customer not found"));
    }

    /**
     * Updates a customer's name, phone, and address without changing its owner.
     *
     * @param businessId ID of the business that must own the customer
     * @param customerId ID of the customer to update
     * @param name new customer name
     * @param phone new WhatsApp phone number
     * @param address new optional address
     * @return the saved customer with an updated timestamp
     */
    public Customer updateCustomer(
            Long businessId,
            Long customerId,
            String name,
            String phone,
            String address) {
        Customer customer = getCustomer(businessId, customerId);
        ensurePhoneIsAvailable(businessId, phone, customerId);

        customer.setName(name);
        customer.setPhone(phone);
        customer.setAddress(address);
        customer.setUpdatedAt(LocalDateTime.now());

        return customerRepository.save(customer);
    }

    // Checks duplicates only within the same business; the database has the same unique rule.
    private void ensurePhoneIsAvailable(
            Long businessId, String phone, Long customerIdToIgnore) {
        boolean phoneAlreadyUsed = customerIdToIgnore == null
                ? customerRepository.existsByBusinessIdAndPhone(businessId, phone)
                : customerRepository.existsByBusinessIdAndPhoneAndIdNot(
                        businessId, phone, customerIdToIgnore);

        if (phoneAlreadyUsed) {
            throw new ResponseStatusException(
                    CONFLICT, "A customer with this phone already exists");
        }
    }
}
