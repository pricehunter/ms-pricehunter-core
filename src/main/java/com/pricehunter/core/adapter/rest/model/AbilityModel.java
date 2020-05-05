package com.pricehunter.core.adapter.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.pricehunter.core.domain.Ability;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class AbilityModel {

    private String name;
    private List<EffectEntriesModel> effectEntries;

    public Ability toAbilityDomain() {

        return Ability.builder()
                .name(name)
                .description(effectEntries.get(0).getEffect())
                .build();
    }
}
