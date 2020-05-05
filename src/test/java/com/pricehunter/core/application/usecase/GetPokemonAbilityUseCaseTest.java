package com.pricehunter.core.application.usecase;

import com.pricehunter.core.domain.Ability;
import com.pricehunter.core.domain.Pokemon;
import com.pricehunter.core.domain.Type;
import com.pricehunter.core.application.port.out.AbilityRepository;
import com.pricehunter.core.application.port.out.PokemonRepository;
import com.pricehunter.core.application.port.out.TypeRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.math.BigDecimal;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("Get PokemonAbility UseCase Test")
@ExtendWith(MockitoExtension.class)
class GetPokemonAbilityUseCaseTest {

    private static final int CORE_POOL_SIZE = 20;
    private static final int MAX_POOL_SIZE = 1000;
    private static final String ASYNC_PREFIX = "async-";
    private static final boolean WAIT_FOR_TASK_TO_COMPLETE_ON_SHUTDOWN = true;
    private static ThreadPoolTaskExecutor executor;

    private AbilityRepository abilityRepository = mock(AbilityRepository.class);
    private TypeRepository typeRepository = mock(TypeRepository.class);
    private PokemonRepository pokemonRepository = mock(PokemonRepository.class);

    @BeforeAll
    static void init() {
        executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(CORE_POOL_SIZE);
        executor.setMaxPoolSize(MAX_POOL_SIZE);
        executor.setWaitForTasksToCompleteOnShutdown(WAIT_FOR_TASK_TO_COMPLETE_ON_SHUTDOWN);
        executor.setThreadNamePrefix(ASYNC_PREFIX);
        executor.initialize();
    }

    @Test
    @DisplayName("When GetPokemonAbilityUseCase is executed Should Return a Pokemon")
    void testPokemonAbility() throws ExecutionException, InterruptedException {

        //given
        var pokemonName = "pokemonName";

        Ability ability = getAbilityMock();
        Type type = getTypeMock();
        Pokemon pokemon = getPokemonMock();

        Pokemon expected = getPokemonMock();

        when(abilityRepository.getAbility(anyString())).thenReturn(ability);
        when(typeRepository.getType(anyString())).thenReturn(type);
        when(pokemonRepository.getPokemon(anyString())).thenReturn(pokemon);

        GetPokemonAbilityUseCase pokemonAbilityUseCase = new GetPokemonAbilityUseCase(pokemonRepository, abilityRepository,
                typeRepository, executor);

        //when
        Pokemon pokemonViewActual = pokemonAbilityUseCase.getPokemon(pokemonName);

        //then
        assertEquals(expected, pokemonViewActual);
    }

    private Ability getAbilityMock() {
        return Ability
                .builder()
                .name("ability1")
                .description("description1")
                .damage(BigDecimal.valueOf(1))
                .build();
    }

    private Type getTypeMock() {
        return Type
                .builder()
                .name("type1")
                .moveDamageClass("moveDamageClass1")
                .build();
    }

    private Pokemon getPokemonMock() {
        return Pokemon
                .builder()
                .name("name1")
                .ability(getAbilityMock())
                .type(getTypeMock())
                .build();
    }

}
