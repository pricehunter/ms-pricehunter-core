package com.pricehunter.core.domain;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Type {
    private String name;
    private String moveDamageClass;
}
