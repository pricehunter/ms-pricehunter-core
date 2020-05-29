package com.pricehunter.core.adapter.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pricehunter.core.adapter.controller.model.PriceRest;
import com.pricehunter.core.application.port.in.ProductPriceHistoryQuery;
import com.pricehunter.core.config.ErrorCode;
import com.pricehunter.core.config.TestConfig;
import com.pricehunter.core.config.exception.ProductNotFoundException;
import com.pricehunter.core.domain.Price;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("ProductPriceHistory Controller Adapter Test")
@WebMvcTest(ProductPriceHistoryControllerAdapter.class)
@Import(TestConfig.class)
class ProductPriceHistoryControllerAdapterTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductPriceHistoryQuery productPriceHistoryQuery;

    @Test
    @DisplayName("when list is called a list is retrieved")
    void listPriceHistoryOK() throws Exception {
        //given
        Long productId = 1L;
        List<Price> mockedList = getPriceList();
        List<PriceRest> expectedResult = PriceRest.toRest(mockedList);
        //when
        when(productPriceHistoryQuery
          .listByProductId(productId))
          .thenReturn(mockedList);
        //then
        this.mockMvc
            .perform(get("/api/v1/products/{id}/prices", productId))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().string(objectMapper.writeValueAsString(expectedResult)));
    }

    private List<Price> getPriceList() {
      return List.of(Price.builder().id(1L).value(123.8).date(LocalDateTime.now()).build());
    }

    @Test
    @DisplayName("when list is called with whrong product id the adapter must return a NotFound exception")
    void listPriceHistoryWithProductNotFound() throws Exception {

        //given
        Long productId = 1L;

        //when
        when(productPriceHistoryQuery.listByProductId(productId))
                .thenThrow(new ProductNotFoundException(ErrorCode.PRODUCT_NOT_FOUND));
        //then
        this.mockMvc.perform(get("/api/v1/products/{name}/prices", productId))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString(ErrorCode.PRODUCT_NOT_FOUND.getReasonPhrase())));
    }

}
