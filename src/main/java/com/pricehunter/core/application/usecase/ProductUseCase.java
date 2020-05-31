package com.pricehunter.core.application.usecase;

import com.pricehunter.core.application.port.in.ProductQuery;
import com.pricehunter.core.application.port.out.ProductRepository;
import com.pricehunter.core.application.usecase.service.ProductService;
import com.pricehunter.core.config.ErrorCode;
import com.pricehunter.core.config.exception.ProductNotFoundException;
import com.pricehunter.core.domain.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ProductUseCase implements ProductQuery {

    private ProductService productService;

    @Autowired
    public ProductUseCase(
      ProductService productService) {
      this.productService = productService;
    }

    @Override
    public Product getById(Long id) {
      return productService.getById(id);
    }
}
