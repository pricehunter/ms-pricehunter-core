package com.pricehunter.core.adapter.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pricehunter.core.adapter.controller.model.AbilityRest;
import com.pricehunter.core.adapter.controller.model.PokemonRest;
import com.pricehunter.core.adapter.controller.model.TypeRest;
import com.pricehunter.core.adapter.rest.exception.NotFoundRestClientException;
import com.pricehunter.core.application.port.in.CreatePokemonCommand;
import com.pricehunter.core.application.port.in.GetPokemonAbilityQuery;
import com.pricehunter.core.config.ErrorCode;
import com.pricehunter.core.config.TestConfig;
import com.pricehunter.core.domain.Ability;
import com.pricehunter.core.domain.Pokemon;
import com.pricehunter.core.domain.Type;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("PokemonController Adapter Test")
@WebMvcTest(PokemonControllerAdapter.class)
@Import(TestConfig.class)
class PokemonControllerAdapterTest {

    private static final String PIKACHU = "pikachu";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private GetPokemonAbilityQuery getPokemonAbilityQuery;

    @MockBean
    private CreatePokemonCommand createPokemonCommand;

    @Test
    @DisplayName("when the getPokemon is called, the adapter must return a pokemon")
    void getPokemonOK() throws Exception {

        when(getPokemonAbilityQuery.getPokemon(anyString())).thenReturn(getPokemonDomain());
        this.mockMvc.perform(get("/api/v1/pokemon/{name}", PIKACHU))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(getPokemonRestExpected())));
    }

    @Test
    @DisplayName("when the getPokemon is called, the adapter must return a NotFound exception")
    void getPokemonNotFound() throws Exception {

        when(getPokemonAbilityQuery.getPokemon(anyString()))
                .thenThrow(new NotFoundRestClientException(ErrorCode.POKEMON_NOT_FOUND));
        this.mockMvc.perform(get("/api/v1/pokemon/{name}", PIKACHU))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString(ErrorCode.POKEMON_NOT_FOUND.getReasonPhrase())));
    }

    private Pokemon getPokemonDomain() {
        Ability ability = Ability.builder()
                .name("thunder")
                .build();
        Type type = Type.builder()
                .name("normal")
                .build();

        return Pokemon.builder()
                .name(PIKACHU)
                .ability(ability)
                .type(type)
                .build();
    }

    private PokemonRest getPokemonRestExpected() {
        AbilityRest abilityView = AbilityRest.builder()
                .name("thunder")
                .build();
        TypeRest typeView = TypeRest.builder()
                .name("normal")
                .build();

        return PokemonRest.builder()
                .name(PIKACHU)
                .ability(abilityView)
                .type(typeView)
                .build();
    }
}
