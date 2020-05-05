package com.pricehunter.core.application.usecase;

import com.pricehunter.core.domain.Ability;
import com.pricehunter.core.domain.Pokemon;
import com.pricehunter.core.domain.Type;
import com.pricehunter.core.application.port.in.GetPokemonAbilityQuery;
import com.pricehunter.core.application.port.out.AbilityRepository;
import com.pricehunter.core.application.port.out.PokemonRepository;
import com.pricehunter.core.application.port.out.TypeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

@Slf4j
@Component
public class GetPokemonAbilityUseCase implements GetPokemonAbilityQuery {

    private PokemonRepository pokemonRepository;
    private AbilityRepository abilityRepository;
    private TypeRepository typeRepository;
    private Executor executor;

    public GetPokemonAbilityUseCase(PokemonRepository pokemonRepository,
                                    AbilityRepository abilityRepository,
                                    TypeRepository typeRepository,
                                    @Qualifier("asyncExecutor") Executor executor) {
        this.pokemonRepository = pokemonRepository;
        this.abilityRepository = abilityRepository;
        this.typeRepository = typeRepository;
        this.executor = executor;
    }

    @Override
    public Pokemon getPokemon(String name) throws ExecutionException, InterruptedException {

        Pokemon pokemon = pokemonRepository.getPokemon(name);
        log.info("Pokemon: {}", pokemon);

        CompletableFuture<Ability> abilityFuture = this.getAbility(pokemon.getAbility().getName());
        CompletableFuture<Type> typeFuture = this.getType(pokemon.getType().getName());

        CompletableFuture.allOf(abilityFuture, typeFuture).join();

        return Pokemon.builder()
                .ability(abilityFuture.get())
                .type(typeFuture.get())
                .name(pokemon.getName())
                .build();
    }

    private CompletableFuture<Ability> getAbility(String name) {
        log.info("Llamado a getAbility, name: {}", name);
        return CompletableFuture
                .supplyAsync(() -> abilityRepository.getAbility(name), executor)
                .exceptionally(ex -> {
                    log.error("Ocurrio un error en el llamado asincrono al repositorio de abilities", ex);
                    return null;
                });
    }

    private CompletableFuture<Type> getType(String name) {
        log.info("Llamado a getType, name: {}", name);
        return CompletableFuture
                .supplyAsync(() -> typeRepository.getType(name), executor)
                .exceptionally(ex -> {
                    log.error("Ocurrio un error en el llamado asincrono al repositorio de types", ex);
                    return null;
                });
    }

}
