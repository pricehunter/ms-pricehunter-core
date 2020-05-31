package com.pricehunter.core.adapter.jpa;

import com.pricehunter.core.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Tests for Product Adapter")
@SpringBootTest()
@SqlGroup(
  value = {
    @Sql(value = "products.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
  }
)
public class ProductAdapterTests {
    public static final String DESCRIPTION = "aDescription";
    public static final String NAME = "aProduct";
    @Autowired
    private ProductAdapter productAdapter;

    @Test()
    @DisplayName("When a product not found a optional empty will be retrieved")
    public void testGetProductNotFound() {
      Long productId = 1L;
      //when
      Optional<Product> product = this.productAdapter.getProductById(productId);
      //then
      assertNotNull(product);
      assertTrue(product.isEmpty());
    }

    @Test()
    @DisplayName("When a product is created")
    public void testCreateProduct() {
      Long productId = 1L;
      //when
      Optional<Product> opProduct = this.productAdapter.getProductById(productId);

      assertNotNull(opProduct);
      assertTrue(opProduct.isEmpty());

      //then
      Long result = this.productAdapter.save(Product.builder().name(NAME).description(DESCRIPTION).build());
      assertEquals(productId, result);
      Optional<Product> savedProduct = this.productAdapter.getProductById(productId);
      assertNotNull(savedProduct);
      assertFalse(savedProduct.isEmpty());
      savedProduct.ifPresent(product -> {
        assertEquals(productId, product.getId());
        assertEquals(NAME, product.getName());
        assertEquals(DESCRIPTION, product.getDescription());
      });
    }

    @Test()
    @DisplayName("When a product is retrieved")
    public void testGetProduct() {
      Long productId = 100L;
      //when
      Optional<Product> opProduct = this.productAdapter.getProductById(productId);
      //then
      assertNotNull(opProduct);
      assertFalse(opProduct.isEmpty());
      opProduct.ifPresent(product -> {
        assertEquals(productId, product.getId());
        assertEquals(NAME, product.getName());
        assertEquals(DESCRIPTION, product.getDescription());
      });
    }
}
