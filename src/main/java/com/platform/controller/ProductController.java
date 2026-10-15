
// Exposes Product Management as HTTP endpoints and passes requests to ProductService.
package com.platform.controller;

import com.platform.entity.Product;
import com.platform.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// @RestController makes return values response bodies (usually JSON).
// @RequestMapping supplies the shared URL prefix; {businessId} is filled from each request URL.
@RestController
@RequestMapping("/api/businesses/{businessId}/products")
public class ProductController {

    private final ProductService productService;

    /**
     * Receives the service through constructor-based dependency injection.
     *
     * @param productService contains product rules and repository access
     */
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Handles POST requests to create a product.
     *
     * @param businessId business ID taken from the URL by @PathVariable
     * @param product product fields parsed from the JSON request body
     * @return the created product as the response body
     */
    // @PostMapping selects HTTP POST; 201 Created tells the caller a new resource was created.
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Product createProduct(
            @PathVariable Long businessId,
            @RequestBody Product product) {
        return productService.createProduct(businessId, product);
    }

    /**
     * Handles GET requests for all products belonging to a business.
     *
     * @param businessId business ID taken from the URL by @PathVariable
     * @return the business's products as the response body
     */
    // @GetMapping selects HTTP GET for the collection URL.
    @GetMapping
    public List<Product> getProducts(
            @PathVariable Long businessId) {
        return productService.getProducts(businessId);
    }

    /**
     * Handles GET requests for one product within a business.
     *
     * @param businessId business ID taken from the URL by @PathVariable
     * @param productId product ID taken from the URL by @PathVariable
     * @return the matching product as the response body
     */
    // The {productId} URL segment is captured by @PathVariable and passed to the service.
    @GetMapping("/{productId}")
    public Product getProduct(
            @PathVariable Long businessId,
            @PathVariable Long productId) {
        return productService.getProduct(businessId, productId);
    }

    /**
     * Handles PUT requests to update one product within a business.
     *
     * @param businessId business ID taken from the URL by @PathVariable
     * @param productId product ID taken from the URL by @PathVariable
     * @param product updated field values parsed from the JSON request body
     * @return the updated product as the response body
     */
    @PutMapping("/{productId}")
    public Product updateProduct(
            @PathVariable Long businessId,
            @PathVariable Long productId,
            @RequestBody Product product) {
        return productService.updateProduct(
                businessId, productId, product);
    }

    /**
     * Handles DELETE requests for one product within a business.
     *
     * @param businessId business ID taken from the URL by @PathVariable
     * @param productId product ID taken from the URL by @PathVariable
     */
    // @ResponseStatus sends 204 No Content, meaning deletion succeeded and no body is returned.
    @DeleteMapping("/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(
            @PathVariable Long businessId,
            @PathVariable Long productId) {
        productService.deleteProduct(businessId, productId);
    }
}
