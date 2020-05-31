package com.pricehunter.core.application.usecase;

import com.pricehunter.core.application.port.in.CreateProductCommand;
import com.pricehunter.core.application.port.out.ProductRepository;
import com.pricehunter.core.domain.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CreateProductUseCase implements CreateProductCommand {

    private ProductRepository productRepository;

    public CreateProductUseCase(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Long create(Command command) {
        log.info("product to save {}", command);
        Product product = buildProduct(command);
        Long result = this.productRepository.save(product);
        log.info("product was save successfully {}", result);
        return result;
    }

  private Product buildProduct(Command command) {
    return Product
      .builder()
      .name(command.getName())
      .description(command.getDescription())
      .build()
      .addPrice(command.getPrice());
  }
}
