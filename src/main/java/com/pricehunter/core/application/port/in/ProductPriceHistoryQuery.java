package com.pricehunter.core.application.port.in;

import com.pricehunter.core.domain.Price;

import java.util.List;

public interface ProductPriceHistoryQuery {

    List<Price> listByProductId(Long productId);
}
