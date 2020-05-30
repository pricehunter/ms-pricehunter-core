package com.pricehunter.core.application.usecase;

import com.pricehunter.core.application.port.in.ProductPriceHistoryQuery;
import com.pricehunter.core.application.port.out.PriceHistoryRepository;
import com.pricehunter.core.application.port.out.ProductRepository;
import com.pricehunter.core.config.ErrorCode;
import com.pricehunter.core.config.exception.ProductNotFoundException;
import com.pricehunter.core.domain.Price;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

@Slf4j
@Component
public class ProductPriceHistoryUseCase implements ProductPriceHistoryQuery {

    private ProductRepository productRepository;
    private PriceHistoryRepository priceHistoryRepository;

    @Autowired
    public ProductPriceHistoryUseCase(
      PriceHistoryRepository priceHistoryRepository,
      ProductRepository productRepository) {
      this.priceHistoryRepository = priceHistoryRepository;
      this.productRepository = productRepository;
    }

    @Override
    public Set<Price> listByProductId(Long productId) {
      log.info("Attempt to find product with id: {}", productId);
      Long savedId = this.productRepository
        .getProductById(productId)
        .orElseThrow(() -> {
          log.error("product with id: {} was not found", productId);
          return new ProductNotFoundException(ErrorCode.PRODUCT_NOT_FOUND);
        })
        .getId();
      log.info("product with id: {} was found", savedId);
      log.info("Attempt to get list of prices for product {}", savedId);
      return this.priceHistoryRepository
        .listByProductId(savedId);
    }
}
