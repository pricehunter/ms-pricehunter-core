package com.pricehunter.core.adapter.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pricehunter.core.adapter.controller.model.ProductRest;
import com.pricehunter.core.application.port.in.CreateProductCommand;
import com.pricehunter.core.application.port.in.ProductQuery;
import com.pricehunter.core.config.TestConfig;
import com.pricehunter.core.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Product Controller Adapter Test")
@WebMvcTest(ProductControllerAdapter.class)
@Import(TestConfig.class)
class ProductControllerAdapterTest {

  public static final String A_NAME = "aName";
  public static final String A_DESCRIPTION = "aDescription";
  @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductQuery productQuery;

    @MockBean
    private CreateProductCommand createProductCommand;

    @Test
    @DisplayName("when product is created ok")
    void createProductOK() throws Exception {
        //given
        CreateProductCommand.Command mockedProduct = CreateProductCommand
          .Command
          .builder()
          .name(A_NAME)
          .description(A_DESCRIPTION)
          .price(123.4)
          .build();
        Long productId = 1L;
        //when
        when(createProductCommand.create(any(CreateProductCommand.Command.class)))
          .thenReturn(productId);
        //then
        this.mockMvc
            .perform(post("/api/v1/products")
              .contentType(MediaType.APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(mockedProduct)))
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(header().string("Location","http://localhost/api/v1/products/1"));
    }

    @Test
    @DisplayName("when a get product is called")
    void testGetProduct() throws Exception {
      //given
      Long productId = 1L;
      Product mockedProduct = Product
        .builder()
        .name(A_NAME)
        .description(A_DESCRIPTION)
        .build();

      ProductRest mockedResponse = ProductRest
        .builder()
        .name(A_NAME)
        .description(A_DESCRIPTION)
        .build();

      //when
      when(this.productQuery.getById(productId)).thenReturn(mockedProduct);

      //then
      this.mockMvc
        .perform(get("/api/v1/products/{id}", productId))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().json(objectMapper.writeValueAsString(mockedResponse)));

      verify(this.productQuery, times(1)).getById(productId);

    }
}
