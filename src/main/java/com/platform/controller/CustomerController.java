// This controller handles HTTP requests for customer records.
// It validates request DTOs, calls CustomerService, and returns response DTOs.
package com.platform.controller;

import com.platform.dto.CustomerRequest;
import com.platform.dto.CustomerResponse;
import com.platform.entity.Customer;
import com.platform.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// Exposes business-scoped customer endpoints and returns customer response DTOs.
@RestController
@RequestMapping("/api/businesses/{businessId}/customers")
public class CustomerController {

    private final CustomerService customerService;

    // Spring supplies the service through constructor-based dependency injection.
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    /**
     * Creates a customer for the business in the URL.
     *
     * @param businessId ID of the business that owns the customer
     * @param request validated customer fields from the JSON body
     * @return response DTO for the saved customer
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerResponse createCustomer(
            @PathVariable Long businessId,
            @Valid @RequestBody CustomerRequest request) {
        Customer customer = customerService.createCustomer(
                businessId,
                request.getName(),
                request.getPhone(),
                request.getAddress());
        return toResponse(customer);
    }

    /**
     * Lists customers that belong to the business in the URL.
     *
     * @param businessId ID of the business whose customers are requested
     * @return response DTOs for that business's customers
     */
    @GetMapping
    public List<CustomerResponse> getCustomers(@PathVariable Long businessId) {
        return customerService.getCustomers(businessId).stream()
                .map(this::toResponse)
                .toList();
    }

    /**
     * Gets one customer only if it belongs to the business in the URL.
     *
     * @param businessId ID of the business that must own the customer
     * @param customerId ID of the requested customer
     * @return response DTO for the customer
     */
    @GetMapping("/{customerId}")
    public CustomerResponse getCustomer(
            @PathVariable Long businessId,
            @PathVariable Long customerId) {
        return toResponse(customerService.getCustomer(businessId, customerId));
    }

    /**
     * Finds a customer by WhatsApp phone number within the business in the URL.
     *
     * @param businessId ID of the business whose customers are searched
     * @param phone phone number supplied as a query parameter
     * @return response DTO for the matching customer
     */
    @GetMapping("/by-phone")
    public CustomerResponse findByPhone(
            @PathVariable Long businessId,
            @RequestParam String phone) {
        return toResponse(customerService.findByPhone(businessId, phone));
    }

    /**
     * Updates a customer's name, phone, and optional address.
     *
     * @param businessId ID of the business that must own the customer
     * @param customerId ID of the customer to update
     * @param request validated replacement fields from the JSON body
     * @return response DTO for the updated customer
     */
    @PutMapping("/{customerId}")
    public CustomerResponse updateCustomer(
            @PathVariable Long businessId,
            @PathVariable Long customerId,
            @Valid @RequestBody CustomerRequest request) {
        Customer customer = customerService.updateCustomer(
                businessId,
                customerId,
                request.getName(),
                request.getPhone(),
                request.getAddress());
        return toResponse(customer);
    }

    // Copies entity fields into a DTO so JPA entities are not returned by the API.
    private CustomerResponse toResponse(Customer customer) {
        return new CustomerResponse(
                customer.getId(),
                customer.getBusinessId(),
                customer.getName(),
                customer.getPhone(),
                customer.getAddress(),
                customer.getCreatedAt(),
                customer.getUpdatedAt());
    }
}
