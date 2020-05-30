package com.pricehunter.core.application.usecase;

import com.pricehunter.core.application.port.out.ProductRepository;
import com.pricehunter.core.application.usecase.service.ProductService;
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

    private ProductService productService = mock(ProductService.class);

    private ProductUseCase useCase;

    @BeforeEach
    public void init() {
      this.useCase = new ProductUseCase(productService);
    }

    @Test
    @DisplayName("When get product returns ok")
    void testProductOK() {

        //given
        var productId = 1L;
        var mockedProduct = getMockedProduct(productId);

        when(productService.getById(productId)).thenReturn(mockedProduct);

        //when
        Product result = this.useCase.getById(productId);

        //then
        assertNotNull(result);
        assertEquals(mockedProduct, result);
        verify(productService, times(1)).getById(productId);
    }

    private Product getMockedProduct(Long id) {
      return Product.builder().id(id).build();
    }

}
