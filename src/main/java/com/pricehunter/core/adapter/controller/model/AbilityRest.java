package com.pricehunter.core.adapter.controller.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.pricehunter.core.domain.Ability;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class AbilityRest {

    private String name;
    private String description;
    private BigDecimal damage;

    public static AbilityRest toAbilityRest(Ability ability) {
        return AbilityRest.builder()
                .name(ability.getName())
                .description(ability.getDescription())
                .damage(ability.getDamage())
                .build();
    }
}
