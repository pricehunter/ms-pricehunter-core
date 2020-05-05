package com.pricehunter.core.adapter.rest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.pricehunter.core.adapter.rest.exception.EmptyOrNullBodyRestClientException;
import com.pricehunter.core.adapter.rest.exception.NotFoundRestClientException;
import com.pricehunter.core.adapter.rest.exception.TimeoutRestClientException;
import com.pricehunter.core.adapter.rest.handler.RestTemplateErrorHandler;
import com.pricehunter.core.domain.Ability;
import com.pricehunter.core.domain.Pokemon;
import com.pricehunter.core.domain.Type;
import com.pricehunter.core.application.port.out.PokemonRepository;
import com.pricehunter.core.config.ErrorCode;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Repository
public class PokemonRestClientAdapter implements PokemonRepository {

    private static final String URL_POKEMON = "https://pokeapi.co/api/v2/pokemon/{name}";
    private final RestTemplate restTemplate;

    public PokemonRestClientAdapter(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        var errorHandler = new RestTemplateErrorHandler(
                Map.of(
                        HttpStatus.NOT_FOUND, new NotFoundRestClientException(ErrorCode.POKEMON_NOT_FOUND),
                        HttpStatus.REQUEST_TIMEOUT, new TimeoutRestClientException(ErrorCode.POKEMON_TIMEOUT)
                )
        );

        this.restTemplate.setErrorHandler(errorHandler);
    }

    @Override
    public Pokemon getPokemon(String name) {
        log.info("PokemonWebService Request: {}", URL_POKEMON);
        PokemonModel response = Optional
                .ofNullable(restTemplate.getForObject(URL_POKEMON, PokemonModel.class, name))
                .orElseThrow(() -> new EmptyOrNullBodyRestClientException(ErrorCode.POKEMON_NOT_FOUND));
        log.info("PokemonWebService Response: " + response);
        return response.toPokemonDomain();
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
    static class PokemonModel {

        private Long id;
        private String name;
        private List<AbilitiesModel> abilities;
        private List<TypesModel> types;

        private Pokemon toPokemonDomain() {

            return Pokemon.builder()
                    .name(name)
                    .ability(abilities.get(0).toAbilitiesDomain())
                    .type(types.get(0).toTypesDomain())
                    .build();
        }
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
    static class AbilitiesModel {

        private AbilityModel ability;

        private Ability toAbilitiesDomain() {
            return Ability.builder()
                    .name(ability.getName())
                    .build();
        }
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
    static class AbilityModel {
        private String name;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
    static class TypesModel {
        private TypeModel type;

        private Type toTypesDomain() {
            return Type.builder()
                    .name(type.getName())
                    .build();
        }
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
    static class TypeModel {
        private String name;
    }
}
