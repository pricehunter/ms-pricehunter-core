package com.pricehunter.core.adapter.controller.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.pricehunter.core.domain.Product;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ProductRest {

    Long id;
    String name;
    String description;

    public static ProductRest toRest(Product product) {
      return ProductRest
        .builder()
        .id(product.getId())
        .name(product.getName())
        .description(product.getDescription())
        .build();
    }

}
