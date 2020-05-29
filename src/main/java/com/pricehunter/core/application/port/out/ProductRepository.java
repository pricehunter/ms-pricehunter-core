package com.pricehunter.core.application.port.out;

import com.pricehunter.core.domain.Product;

import java.util.Optional;

public interface ProductRepository {

    Optional<Product> getProductById(Long id);

    Product save(Product product);
}
