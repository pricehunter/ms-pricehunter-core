package com.pricehunter.core.application.usecase;

import com.pricehunter.core.application.port.in.CreatePokemonCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
public class CreatePokemonUseCase implements CreatePokemonCommand {

    @Override
    public UUID createPokemon(Command command) {

        log.info("Pokemon a crear {}", command.getPokemon());

        return UUID.randomUUID();
    }
}
