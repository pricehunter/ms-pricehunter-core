package com.pricehunter.core.application.port.in;

import com.pricehunter.core.domain.Pokemon;
import lombok.Builder;
import lombok.Value;

import java.util.UUID;

public interface CreatePokemonCommand {

    UUID createPokemon(Command command);

    @Value
    @Builder
    class Command {
       Pokemon pokemon;
    }
}
