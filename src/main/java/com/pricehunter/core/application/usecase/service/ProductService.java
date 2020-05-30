package com.pricehunter.core.application.usecase.service;

import com.pricehunter.core.application.port.out.ProductRepository;
import com.pricehunter.core.config.ErrorCode;
import com.pricehunter.core.config.exception.ProductNotFoundException;
import com.pricehunter.core.domain.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ProductService {

    private ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
      this.productRepository = productRepository;
    }

    public Product getById(Long id) {
      log.info("Attempt to find product with id: {}", id);
      Product savedProduct = this
        .productRepository
        .getProductById(id)
        .orElseThrow(() -> {
          log.error("product with id: {} was not found", id);
          return new ProductNotFoundException(ErrorCode.PRODUCT_NOT_FOUND);
        });
      log.info("product with id: {} was found", savedProduct);
      return savedProduct;
    }
}
