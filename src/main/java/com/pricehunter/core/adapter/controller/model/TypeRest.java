package com.pricehunter.core.adapter.controller.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.pricehunter.core.domain.Type;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class TypeRest {

    private String name;
    private String moveDamageClass;

    public static TypeRest toTypeRest(Type type) {
        return TypeRest.builder()
                .name(type.getName())
                .moveDamageClass(type.getMoveDamageClass())
                .build();
    }
}
