
// This service handles product operations and connects the controller to the repositories.
// It also creates the initial inventory record when a new product is saved.
package com.platform.service;

import com.platform.entity.Product;
import com.platform.entity.Inventory;
import com.platform.repository.InventoryRepository;
import com.platform.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;

// @Service marks this class as application logic managed by Spring.
// ProductController calls this service to create, read, update, and delete products.
@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;

    /**
     * Receives both repositories through constructor-based dependency injection.
     * Spring supplies the database operations needed to create a product and its inventory.
     *
     * @param productRepository database operations for products
     * @param inventoryRepository database operations for inventory records
     */
    public ProductService(
            ProductRepository productRepository,
            InventoryRepository inventoryRepository) {
        this.productRepository = productRepository;
        this.inventoryRepository = inventoryRepository;
    }

    /**
     * Creates a product for the requested business and initializes its timestamps.
     *
     * @param businessId ID of the business that will own the product
     * @param product product details received from the controller
     * @return the saved product, including its generated database ID
     */
    // Keep both database writes in one transaction so they succeed or roll back together.
    @Transactional
    public Product createProduct(Long businessId, Product product) {
        // Ignore any client-supplied ID so the database can generate a new one.
        product.setId(null);
        product.setBusinessId(businessId);

        LocalDateTime now = LocalDateTime.now();
        // Use one timestamp for both fields so a newly created product starts consistently.
        product.setCreatedAt(now);
        product.setUpdatedAt(now);

        Product savedProduct = productRepository.save(product);

        // Use the generated product ID and the same timestamp for its initial zero-stock record.
        Inventory inventory = new Inventory();
        inventory.setProductId(savedProduct.getId());
        inventory.setQuantity(BigDecimal.ZERO);
        inventory.setUpdatedAt(now);
        inventoryRepository.save(inventory);

        return savedProduct;
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
