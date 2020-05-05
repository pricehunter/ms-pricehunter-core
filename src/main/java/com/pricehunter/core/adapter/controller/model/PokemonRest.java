package com.pricehunter.core.adapter.controller.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.pricehunter.core.domain.Pokemon;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class PokemonRest {

    private String name;
    private AbilityRest ability;
    private TypeRest type;

    public static PokemonRest toPokemonRest(Pokemon pokemon) {
        return PokemonRest.builder()
                .name(pokemon.getName())
                .ability(AbilityRest.toAbilityRest(pokemon.getAbility()))
                .type(TypeRest.toTypeRest(pokemon.getType()))
                .build();
    }
}
