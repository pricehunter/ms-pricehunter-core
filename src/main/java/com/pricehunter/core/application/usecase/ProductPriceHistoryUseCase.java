package com.pricehunter.core.application.usecase;

import com.pricehunter.core.application.port.in.ProductPriceHistoryQuery;
import com.pricehunter.core.application.port.out.PriceHistoryRepository;
import com.pricehunter.core.application.port.out.ProductRepository;
import com.pricehunter.core.domain.Price;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

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
    public List<Price> listByProductId(Long productId) {
      log.info("Attempt to find product with id: {}", productId);
      this.productRepository
        .getProductById(productId)
        .orElseThrow(RuntimeException::new);
      log.info("product with id: {} was found", productId);
      log.info("Attempt to get list of prices for product {}", productId);
      return this.priceHistoryRepository
        .listByProductId(productId);
    }
}
