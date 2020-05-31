package com.pricehunter.core.adapter.jpa;

import com.pricehunter.core.domain.Price;
import com.pricehunter.core.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Tests for Price History Adapter")
@SpringBootTest()
@Sql({"products.sql", "prices.sql"})
public class PriceHistoryAdapterTests {

    @Autowired
    private PriceHistoryAdapter priceHistoryAdapter;

    @Test()
    @DisplayName("When a price list is found will be retrieved")
    public void testGetPricesForProduct() {
      Long productId = 100L;
      //when
      Set<Price> prices = this.priceHistoryAdapter.listByProductId(productId);
      //then
      assertNotNull(prices);
      assertFalse(prices.isEmpty());
      assertEquals(prices.size(), 1L );
    }

    @Test()
    @DisplayName("When a product not exists empty list will be retrieved")
    public void testGetPricesForNotFoundProduct() {
      Long productId = 1L;
      //when
      Set<Price> prices = this.priceHistoryAdapter.listByProductId(productId);
      //then
      assertNotNull(prices);
      assertTrue(prices.isEmpty());
    }
}
