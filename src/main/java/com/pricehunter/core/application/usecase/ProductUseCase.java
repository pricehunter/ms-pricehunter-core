package com.pricehunter.core.application.usecase;

import com.pricehunter.core.application.port.in.ProductQuery;
import com.pricehunter.core.application.port.out.ProductRepository;
import com.pricehunter.core.config.ErrorCode;
import com.pricehunter.core.config.exception.ProductNotFoundException;
import com.pricehunter.core.domain.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ProductUseCase implements ProductQuery {

    private ProductRepository productRepository;

    @Autowired
    public ProductUseCase(
      ProductRepository productRepository) {
      this.productRepository = productRepository;
    }

    @Override
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
