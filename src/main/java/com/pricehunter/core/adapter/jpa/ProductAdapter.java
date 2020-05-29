package com.pricehunter.core.adapter.jpa;

import com.pricehunter.core.application.port.out.ProductRepository;
import com.pricehunter.core.domain.Product;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class ProductAdapter implements ProductRepository {

  private final ProductJPARepository productRepository;

  public ProductAdapter(ProductJPARepository productJPARepository) {
      this.productRepository = productJPARepository;
  }

  @Override
  public Optional<Product> getProductById(Long id) {
      return this.productRepository.findById(id);
  }

  @Override
  public Product save(Product product) {
    return this.productRepository.save(product);
  }
}
