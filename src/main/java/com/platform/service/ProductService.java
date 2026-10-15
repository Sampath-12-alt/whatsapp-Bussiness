
// Contains Product Management rules and connects the HTTP-facing controller to the database repository.
package com.platform.service;

import com.platform.entity.Product;
import com.platform.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;

// @Service marks this class as application logic managed by Spring.
@Service
public class ProductService {

    private final ProductRepository productRepository;

    /**
     * Receives the repository through constructor-based dependency injection.
     * Spring supplies the repository, and the final field keeps that dependency fixed.
     *
     * @param productRepository database operations for products
     */
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * Creates a product for the requested business and initializes its timestamps.
     *
     * @param businessId ID of the business that will own the product
     * @param product product details received from the controller
     * @return the saved product, including its generated database ID
     */
    public Product createProduct(Long businessId, Product product) {
        // Ignore any client-supplied ID so the database can generate a new one.
        product.setId(null);
        product.setBusinessId(businessId);

        LocalDateTime now = LocalDateTime.now();
        // Use one timestamp for both fields so a newly created product starts consistently.
        product.setCreatedAt(now);
        product.setUpdatedAt(now);

        return productRepository.save(product);
    }

    /**
     * Retrieves every product belonging to one business.
     *
     * @param businessId ID of the business to filter by
     * @return that business's products, or an empty list when it has none
     */
    public List<Product> getProducts(Long businessId) {
        return productRepository.findByBusinessId(businessId);
    }

    /**
     * Retrieves one product while also checking that it belongs to the given business.
     *
     * @param businessId ID of the business that must own the product
     * @param productId ID of the requested product
     * @return the matching product
     * @throws ResponseStatusException with HTTP 404 when no matching product exists
     */
    public Product getProduct(Long businessId, Long productId) {
        return productRepository
                // Optional represents a result that may be present or absent; do not treat another business's product as a match.
                .findByIdAndBusinessId(productId, businessId)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                NOT_FOUND, "Product not found"));
    }

    /**
     * Updates editable product fields without changing its ID, owner, or creation time.
     *
     * @param businessId ID of the business that must own the product
     * @param productId ID of the product to update
     * @param request incoming values for the editable product fields
     * @return the saved product with its updated timestamp
     * @throws ResponseStatusException with HTTP 404 when the product is not found for that business
     */
    public Product updateProduct(
            Long businessId, Long productId, Product request) {

        // Load by both IDs before updating so the operation stays scoped to the business.
        Product product = getProduct(businessId, productId);

        product.setCategoryId(request.getCategoryId());
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setUnit(request.getUnit());
        product.setAvailable(request.getAvailable());
        product.setUpdatedAt(LocalDateTime.now());

        return productRepository.save(product);
    }

    /**
     * Deletes a product only if it belongs to the specified business.
     *
     * @param businessId ID of the business that must own the product
     * @param productId ID of the product to delete
     * @throws ResponseStatusException with HTTP 404 when the product is not found for that business
     */
    public void deleteProduct(Long businessId, Long productId) {
        Product product = getProduct(businessId, productId);
        productRepository.delete(product);
    }
}
