package com.pricehunter.core.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Pokemon Domain Test")
class PokemonTest {

    @Test
    @DisplayName("When a pokemon attack then should return a remaining health to the target")
    void testPokemonAttack() {
        //given
        var myPokemon = buildPokemon();

        //when
        myPokemon.attack(BigDecimal.valueOf(100));

        //then
        assertEquals(BigDecimal.valueOf(400), myPokemon.getHealth());
    }

    private Pokemon buildPokemon() {
        return Pokemon.builder()
                .name("pikachu")
                .health(BigDecimal.valueOf(500))
                .build();
    }

}