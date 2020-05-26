package com.pricehunter.core.application.port.out;

import com.pricehunter.core.domain.Price;

import java.util.List;

public interface PriceHistoryRepository {

    List<Price> listByProductId(Long productId);
}
