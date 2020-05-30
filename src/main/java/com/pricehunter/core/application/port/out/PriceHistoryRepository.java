package com.pricehunter.core.application.port.out;

import com.pricehunter.core.domain.Price;

import java.util.List;
import java.util.Set;

public interface PriceHistoryRepository {

    Set<Price> listByProductId(Long productId);
}
