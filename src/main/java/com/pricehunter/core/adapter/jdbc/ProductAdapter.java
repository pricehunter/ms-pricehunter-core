package com.pricehunter.core.adapter.jdbc;

import com.pricehunter.core.application.port.out.ProductRepository;
import com.pricehunter.core.domain.Product;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class ProductAdapter implements ProductRepository {

  @Override
  public Optional<Product> getProductById(Long id) {
    return Optional.of(Product
      .builder()
      .id(1L)
      .name("someName")
      .description("someDescription")
      .build());
  }
}
