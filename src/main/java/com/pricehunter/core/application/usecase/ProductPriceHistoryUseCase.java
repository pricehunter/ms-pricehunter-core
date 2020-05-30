package com.pricehunter.core.application.usecase;

import com.pricehunter.core.application.port.in.ProductPriceHistoryQuery;
import com.pricehunter.core.application.port.out.PriceHistoryRepository;
import com.pricehunter.core.application.port.out.ProductRepository;
import com.pricehunter.core.application.usecase.service.ProductService;
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

    private ProductService productService;
    private PriceHistoryRepository priceHistoryRepository;

    @Autowired
    public ProductPriceHistoryUseCase(
      PriceHistoryRepository priceHistoryRepository,
      ProductService productService) {
      this.priceHistoryRepository = priceHistoryRepository;
      this.productService = productService;
    }

    @Override
    public Set<Price> listByProductId(Long productId) {
      Long savedId = this.productService.getById(productId).getId();
      log.info("Attempt to get list of prices for product {}", savedId);
      return this.priceHistoryRepository
        .listByProductId(savedId);
    }
}
