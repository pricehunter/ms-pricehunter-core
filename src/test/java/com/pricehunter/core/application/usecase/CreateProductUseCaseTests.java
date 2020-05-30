package com.pricehunter.core.application.usecase;

import com.pricehunter.core.application.port.in.CreateProductCommand;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Create Product UseCase Test")
@ExtendWith(MockitoExtension.class)
class CreateProductUseCaseTests {

    private ProductRepository productRepository = mock(ProductRepository.class);

    private CreateProductUseCase useCase;

    @BeforeEach
    public void init() {
      this.useCase = new CreateProductUseCase(productRepository);
    }

    @Test
    @DisplayName("When crete product ok")
    void testCreateProduct() {

        //given
        var productId = 1L;
        var mockedCommand = CreateProductCommand
          .Command
          .builder()
          .name("aName")
          .description("aDescription")
          .build();

        when(productRepository.save(any(Product.class))).thenReturn(productId);

        //when
        Long result = this.useCase.create(mockedCommand);

        //then
        assertNotNull(result);
        assertEquals(productId, result);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    private Product getMockedProduct(Long id) {
      return Product.builder().id(id).build();
    }

}
