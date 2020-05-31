package com.pricehunter.core.application.usecase.service;

import com.pricehunter.core.application.port.out.ProductRepository;
import com.pricehunter.core.config.ErrorCode;
import com.pricehunter.core.config.exception.ProductNotFoundException;
import com.pricehunter.core.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Product Service Test")
@ExtendWith(MockitoExtension.class)
public class ProductServiceTests {

    private ProductService productService;
    private ProductRepository mockedProductRepository;

    @BeforeEach
    public void init() {
        this.mockedProductRepository = mock(ProductRepository.class);
        this.productService = new ProductService(this.mockedProductRepository);
    }

    @Test
    @DisplayName("when a product exists is returned")
    public void testGetProduct() {
      //given
      Long productId = 1L;
      Product mockedProduct = Product.builder().build();
      when(mockedProductRepository.getProductById(productId))
        .thenReturn(Optional.of(mockedProduct));

      //when
      Product result = this.productService.getById(productId);

      //then
      assertNotNull(result);
      assertEquals(mockedProduct, result);
      verify(this.mockedProductRepository, times(1)).getProductById(productId);
    }

    @Test
    @DisplayName("when a product not exists exception is thrown")
    public void testGetNonExistProduct() {
      //given
      Long productId = 1L;
      when(mockedProductRepository.getProductById(productId)).thenReturn(Optional.empty());

      //when
      ProductNotFoundException result = assertThrows(
        ProductNotFoundException.class, () -> this.productService.getById(productId));

      //then
      assertNotNull(result);
      assertEquals(ErrorCode.PRODUCT_NOT_FOUND, result.getCode());
      verify(this.mockedProductRepository, times(1)).getProductById(productId);
    }


}
