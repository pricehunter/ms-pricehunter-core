package com.pricehunter.core.application.usecase;

import com.pricehunter.core.application.port.in.ProductPriceHistoryQuery;
import com.pricehunter.core.application.port.out.PriceHistoryRepository;
import com.pricehunter.core.domain.Price;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class ProductPriceHistoryUseCase implements ProductPriceHistoryQuery {

    private PriceHistoryRepository priceHistoryRepository;

    @Autowired
    public ProductPriceHistoryUseCase(PriceHistoryRepository priceHistoryRepository) {
      this.priceHistoryRepository = priceHistoryRepository;
    }

    @Override
    public List<Price> listByProductId(Long productId) {
      return this.priceHistoryRepository.listByProductId(productId);
    }
}
