package com.ecommerce.service;

import com.ecommerce.dto.ProductRequest;
import com.ecommerce.dto.ProductResponse;
import com.ecommerce.entity.Product;
import com.ecommerce.repository.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductResponse createProduct(ProductRequest request) {
        if (Objects.isNull(request.getName()) || request.getName().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product name is required");
        }
        if (Objects.isNull(request.getPrice())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product price is required");
        }
        Product product = new Product();
        applyRequest(product, request);
        return ProductResponse.fromEntity(productRepository.save(product));
    }

    @Transactional(readOnly = true)
    public ProductResponse getProductById(Long productId) {
        return ProductResponse.fromEntity(getProductOrThrow(productId));
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(ProductResponse::fromEntity)
                .toList();
    }

    @Transactional
    public ProductResponse updateProduct(Long productId, ProductRequest request) {
        Product product = getProductOrThrow(productId);
        applyRequest(product, request);
        return ProductResponse.fromEntity(productRepository.save(product));
    }

    @Transactional
    public void deleteProduct(Long productId) {
        getProductOrThrow(productId);
        productRepository.deleteById(productId);
    }

    public Product getProductOrThrow(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Product not found for id: " + productId));
    }

    private void applyRequest(Product product, ProductRequest request) {
        setIfNotNull(request.getName(), product::setName);
        setIfNotNull(request.getBrandName(), product::setBrandName);
        setIfNotNull(request.getModel(), product::setModel);
        setIfNotNull(request.getPrice(), product::setPrice);
        setIfNotNull(request.getDiscount(), product::setDiscount);
    }

    private <T> void setIfNotNull(T value, Consumer<T> setter) {
        if (Objects.nonNull(value)) {
            setter.accept(value);
        }
    }
}