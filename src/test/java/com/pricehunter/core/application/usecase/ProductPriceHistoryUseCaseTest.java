package com.pricehunter.core.application.usecase;

import com.pricehunter.core.application.port.out.PriceHistoryRepository;
import com.pricehunter.core.application.port.out.ProductRepository;
import com.pricehunter.core.config.ErrorCode;
import com.pricehunter.core.config.exception.ProductNotFoundException;
import com.pricehunter.core.domain.Price;
import com.pricehunter.core.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Product Price History UseCase Test")
@ExtendWith(MockitoExtension.class)
class ProductPriceHistoryUseCaseTest {

    private ProductRepository productRepository = mock(ProductRepository.class);
    private PriceHistoryRepository priceHistoryRepository = mock(PriceHistoryRepository.class);

    private ProductPriceHistoryUseCase useCase;

    @BeforeEach
    public void init() {
      this.useCase = new ProductPriceHistoryUseCase(priceHistoryRepository, productRepository);
    }

    @Test
    @DisplayName("When get product price history returns ok")
    void testProductPriceHistoryOK() {

        //given
        var productId = 1L;
        var mockedProduct = Optional.of(getMockedProduct(productId));
        var mockedPriceList = List.of(getMockedPrice());

        when(productRepository.getProductById(productId)).thenReturn(mockedProduct);
        when(priceHistoryRepository.listByProductId(productId)).thenReturn(mockedPriceList);

        //when
        List<Price> result = this.useCase.listByProductId(productId);

        //then
        assertNotNull(result);
        assertEquals(mockedPriceList, result);
        verify(productRepository, times(1)).getProductById(productId);
        verify(priceHistoryRepository, times(1)).listByProductId(productId);
    }

    @Test()
    @DisplayName("When a product id dont exists exception will occur")
    void testProductIdDontExists() {
        //given
        var productId = 1L;
        when(productRepository.getProductById(productId)).thenReturn(Optional.empty());
        //when
        ProductNotFoundException result = assertThrows(ProductNotFoundException.class, () -> this.useCase.listByProductId(productId));
        //then
        assertNotNull(result);
        assertEquals(ErrorCode.PRODUCT_NOT_FOUND, result.getCode());
        verify(productRepository, times(1)).getProductById(productId);
        verify(priceHistoryRepository, never()).listByProductId(productId);

    }

    private Price getMockedPrice() {
      return Price.builder().build();
    }

    private Product getMockedProduct(Long id) {
      return Product.builder().id(id).build();
    }

}
