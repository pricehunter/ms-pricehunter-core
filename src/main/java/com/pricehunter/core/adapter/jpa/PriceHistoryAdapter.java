package com.pricehunter.core.adapter.jpa;

import com.pricehunter.core.application.port.out.PriceHistoryRepository;
import com.pricehunter.core.domain.Price;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public class PriceHistoryAdapter implements PriceHistoryRepository {

  private PriceJPARepository priceJPARepository;

  public PriceHistoryAdapter(PriceJPARepository priceJPARepository) {
    this.priceJPARepository = priceJPARepository;
  }

  @Override
  public Set<Price> listByProductId(Long productId) {
    return this.priceJPARepository.findAllByProductId(productId);
  }
}
