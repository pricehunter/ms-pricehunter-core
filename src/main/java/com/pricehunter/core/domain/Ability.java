package com.pricehunter.core.domain;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
public class Ability {
    private String name;
    private String description;
    private BigDecimal damage;
}
