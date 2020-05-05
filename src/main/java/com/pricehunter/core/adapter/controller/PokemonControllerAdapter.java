package com.pricehunter.core.adapter.controller;

import com.pricehunter.core.adapter.controller.model.PokemonRest;
import com.pricehunter.core.application.port.in.CreatePokemonCommand;
import com.pricehunter.core.application.port.in.GetPokemonAbilityQuery;
import com.pricehunter.core.domain.Pokemon;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/v1/pokemon")
@Slf4j
public final class PokemonControllerAdapter {

    private GetPokemonAbilityQuery getPokemonAbilityQuery;
    private CreatePokemonCommand createPokemonCommand;

    public PokemonControllerAdapter(GetPokemonAbilityQuery getPokemonAbilityQuery,
                                    CreatePokemonCommand createPokemonCommand) {
        this.getPokemonAbilityQuery = getPokemonAbilityQuery;
        this.createPokemonCommand = createPokemonCommand;
    }

    @GetMapping("/{name}")
    public PokemonRest getPokemon(@PathVariable("name") String name) throws ExecutionException, InterruptedException {

        log.info("Llamada al servicio /pokemon/{}", name);
        Pokemon pokemon = this.getPokemonAbilityQuery.getPokemon(name);
        PokemonRest pokemonView = PokemonRest.toPokemonRest(pokemon);
        log.info("Respuesta servicio getPokemon: {}", pokemonView);

        return pokemonView;
    }

    @PostMapping
    public UUID createPokemon() {
        Pokemon pokemon = Pokemon.builder()
                .name("pikachu")
                .build();

        CreatePokemonCommand.Command command = CreatePokemonCommand.Command.builder()
                .pokemon(pokemon)
                .build();

        return createPokemonCommand.createPokemon(command);
    }

}

