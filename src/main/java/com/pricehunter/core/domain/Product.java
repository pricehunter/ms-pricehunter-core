package com.pricehunter.core.domain;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder
public class Product {
    Long id;
    String name;
    String description;
}
