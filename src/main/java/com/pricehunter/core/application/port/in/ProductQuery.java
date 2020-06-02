package com.pricehunter.core.application.port.in;

import com.pricehunter.core.domain.Product;

public interface ProductQuery {

    Product getById(Long id);
}
