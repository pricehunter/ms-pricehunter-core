package com.pricehunter.core.adapter.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pricehunter.core.adapter.rest.exception.EmptyOrNullBodyRestClientException;
import com.pricehunter.core.adapter.rest.exception.NotFoundRestClientException;
import com.pricehunter.core.domain.Ability;
import com.pricehunter.core.domain.Pokemon;
import com.pricehunter.core.domain.Type;
import com.pricehunter.core.config.ErrorCode;
import com.pricehunter.core.config.TestConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;

import java.util.List;

import static com.pricehunter.core.adapter.rest.PokemonRestClientAdapter.AbilitiesModel;
import static com.pricehunter.core.adapter.rest.PokemonRestClientAdapter.AbilityModel;
import static com.pricehunter.core.adapter.rest.PokemonRestClientAdapter.PokemonModel;
import static com.pricehunter.core.adapter.rest.PokemonRestClientAdapter.TypeModel;
import static com.pricehunter.core.adapter.rest.PokemonRestClientAdapter.TypesModel;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withNoContent;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@DisplayName("PokemonRestClient Adapter Test")
@Import(TestConfig.class)
@RestClientTest({PokemonRestClientAdapter.class})
class PokemonRestClientAdapterTest {

    private static final String EXPECTED_URI = "https://pokeapi.co/api/v2/pokemon/pikachu";

    @Autowired
    private PokemonRestClientAdapter client;

    @Autowired
    private MockRestServiceServer server;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    @DisplayName("when the getPokemon is called, the adapter must return a EmptyOrNullBodyRestClientException")
    void tesGetPokemonEmptyOrNullBodyRestClientException() {

        //given
        this.server.expect(requestTo(EXPECTED_URI)).andRespond(withNoContent());

        //when
        Throwable thrown = catchThrowable(() -> this.client.getPokemon("pikachu"));

        //then
        assertThat(thrown)
                .isInstanceOf(EmptyOrNullBodyRestClientException.class)
                .hasMessage(ErrorCode.POKEMON_NOT_FOUND.getReasonPhrase());
    }

    @Test
    @DisplayName("when the getPokemon is called, the adapter must return a NotFoundRestClientException")
    void testGetPokemonNotFoundRestClientException() {

        //given
        this.server.expect(requestTo(EXPECTED_URI)).andRespond(withStatus(HttpStatus.NOT_FOUND));

        //when
        Throwable thrown = catchThrowable(() -> this.client.getPokemon("pikachu"));

        //then
        assertThat(thrown)
                .isInstanceOf(NotFoundRestClientException.class)
                .hasMessage(ErrorCode.POKEMON_NOT_FOUND.getReasonPhrase());
    }

    @Test
    @DisplayName("when the getPokemon is called, the adapter must return a Pokemon domain object")
    void testGetPokemonNormalCase() throws JsonProcessingException {

        //given
        Pokemon expectedResponse = getExpectedDomainForMockedResponse();
        String detailsString = objectMapper.writeValueAsString(getMockedResponse());

        this.server.expect(requestTo(EXPECTED_URI))
                .andRespond(withSuccess(detailsString, MediaType.APPLICATION_JSON));

        //when
        Pokemon currentResponse = this.client.getPokemon("pikachu");

        //then
        assertThat(currentResponse).isEqualTo(expectedResponse);
    }

    private PokemonModel getMockedResponse() {
        PokemonModel pokemonModel = new PokemonModel();
        AbilitiesModel abilitiesModel = new AbilitiesModel();
        TypesModel typesModel = new TypesModel();
        AbilityModel abilityModel = new AbilityModel();
        TypeModel typeModel = new TypeModel();

        abilityModel.setName("thunder");
        typeModel.setName("normal");

        abilitiesModel.setAbility(abilityModel);
        typesModel.setType(typeModel);

        pokemonModel.setId(1L);
        pokemonModel.setName("pikachu");
        pokemonModel.setAbilities(List.of(abilitiesModel));
        pokemonModel.setTypes(List.of(typesModel));

        return pokemonModel;
    }

    private Pokemon getExpectedDomainForMockedResponse() {
        Ability ability = Ability.builder().name("thunder").build();
        Type type = Type.builder().name("normal").build();

        return Pokemon.builder()
                .name("pikachu")
                .type(type)
                .ability(ability)
                .build();
    }
}
