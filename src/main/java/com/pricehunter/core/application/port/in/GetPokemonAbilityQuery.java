package com.pricehunter.core.application.port.in;

import com.pricehunter.core.domain.Pokemon;

import java.util.concurrent.ExecutionException;

public interface GetPokemonAbilityQuery {

    Pokemon getPokemon(String name) throws ExecutionException, InterruptedException;
}
