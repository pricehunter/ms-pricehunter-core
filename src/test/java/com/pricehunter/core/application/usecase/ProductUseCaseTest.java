package com.pricehunter.core.application.usecase;

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

@DisplayName("Product Price History UseCase Test")
@ExtendWith(MockitoExtension.class)
class ProductUseCaseTest {

    private ProductRepository productRepository = mock(ProductRepository.class);

    private ProductUseCase useCase;

    @BeforeEach
    public void init() {
      this.useCase = new ProductUseCase(productRepository);
    }

    @Test
    @DisplayName("When get product returns ok")
    void testProductOK() {

        //given
        var productId = 1L;
        var mockedProduct = getMockedProduct(productId);

        when(productRepository.getProductById(productId)).thenReturn(Optional.of(mockedProduct));

        //when
        Product result = this.useCase.getById(productId);

        //then
        assertNotNull(result);
        assertEquals(mockedProduct, result);
        verify(productRepository, times(1)).getProductById(productId);
    }

    @Test()
    @DisplayName("When a product id dont exists exception will occur")
    void testProductIdDontExists() {
        //given
        var productId = 1L;
        when(productRepository.getProductById(productId)).thenReturn(Optional.empty());
        //when
        ProductNotFoundException result = assertThrows(ProductNotFoundException.class, () -> this.useCase.getById(productId));
        //then
        assertNotNull(result);
        assertEquals(ErrorCode.PRODUCT_NOT_FOUND, result.getCode());
        verify(productRepository, times(1)).getProductById(productId);

    }

    private Product getMockedProduct(Long id) {
      return Product.builder().id(id).build();
    }

}
