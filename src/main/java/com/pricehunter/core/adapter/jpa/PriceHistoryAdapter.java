package com.pricehunter.core.adapter.jpa;

import com.pricehunter.core.application.port.out.PriceHistoryRepository;
import com.pricehunter.core.domain.Price;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class PriceHistoryAdapter implements PriceHistoryRepository {

  @Override
  public List<Price> listByProductId(Long productId) {
    return List
      .of(Price
        .builder()
        .id(1L)
        .date(LocalDateTime.now())
        .value(123.5)
        .build());
  }
}
