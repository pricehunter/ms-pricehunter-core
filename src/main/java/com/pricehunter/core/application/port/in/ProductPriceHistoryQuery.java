package com.pricehunter.core.application.port.in;

import com.pricehunter.core.domain.Price;

import java.util.Set;

public interface ProductPriceHistoryQuery {

    Set<Price> listByProductId(Long productId);
}
