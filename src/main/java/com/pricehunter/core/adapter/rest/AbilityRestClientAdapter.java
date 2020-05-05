package com.pricehunter.core.adapter.rest;

import com.pricehunter.core.adapter.rest.exception.NotFoundRestClientException;
import com.pricehunter.core.adapter.rest.exception.TimeoutRestClientException;
import com.pricehunter.core.adapter.rest.handler.RestTemplateErrorHandler;
import com.pricehunter.core.adapter.rest.model.AbilityModel;
import com.pricehunter.core.adapter.rest.exception.EmptyOrNullBodyRestClientException;
import com.pricehunter.core.domain.Ability;
import com.pricehunter.core.application.port.out.AbilityRepository;
import com.pricehunter.core.config.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Optional;

@Repository
@Slf4j
public class AbilityRestClientAdapter implements AbilityRepository {

    private static final String API_URL = "https://pokeapi.co/api/v2/ability/{ability}";
    private final RestTemplate restTemplate;

    public AbilityRestClientAdapter(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        var errorHandler = new RestTemplateErrorHandler(
                Map.of(
                        HttpStatus.NOT_FOUND, new NotFoundRestClientException(ErrorCode.ABILITY_NOT_FOUND),
                        HttpStatus.REQUEST_TIMEOUT, new TimeoutRestClientException(ErrorCode.ABILITY_TIMEOUT)
                )
        );
        this.restTemplate.setErrorHandler(errorHandler);
    }

    @Override
    public Ability getAbility(String name) {

        log.info("AbilityWebService Request: {}", API_URL);
        AbilityModel response = Optional.ofNullable(restTemplate.getForObject(API_URL, AbilityModel.class, name))
                .orElseThrow(() -> new EmptyOrNullBodyRestClientException(ErrorCode.ABILITY_NOT_FOUND));
        log.info("Llamado a ability por rest terminado, {}", response);
        return response.toAbilityDomain();
    }

}
