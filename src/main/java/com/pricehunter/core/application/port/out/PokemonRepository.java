package com.pricehunter.core.application.port.out;

import com.pricehunter.core.domain.Pokemon;

public interface PokemonRepository {

    Pokemon getPokemon(String name);
}
