package com.pricehunter.core.adapter.rest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.pricehunter.core.adapter.rest.exception.EmptyOrNullBodyRestClientException;
import com.pricehunter.core.adapter.rest.exception.NotFoundRestClientException;
import com.pricehunter.core.adapter.rest.exception.TimeoutRestClientException;
import com.pricehunter.core.adapter.rest.handler.RestTemplateErrorHandler;
import com.pricehunter.core.domain.Type;
import com.pricehunter.core.application.port.out.TypeRepository;
import com.pricehunter.core.config.ErrorCode;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Optional;

@Repository
@Slf4j
public class TypeRestClientAdapter implements TypeRepository {

    private static final String URL_API = "https://pokeapi.co/api/v2/type/{type}";
    private final RestTemplate restTemplate;

    public TypeRestClientAdapter(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        var errorHandler = new RestTemplateErrorHandler(
                Map.of(
                        HttpStatus.NOT_FOUND, new NotFoundRestClientException(ErrorCode.TYPE_NOT_FOUND),
                        HttpStatus.REQUEST_TIMEOUT, new TimeoutRestClientException(ErrorCode.TYPE_TIMEOUT)
                )
        );

        this.restTemplate.setErrorHandler(errorHandler);
    }

    @Override
    public Type getType(String typeName) {

        log.info("TypeWebService Request: {}", URL_API);
        TypeModel response = Optional.ofNullable(restTemplate.getForObject(URL_API, TypeModel.class, typeName))
                .orElseThrow(() -> new EmptyOrNullBodyRestClientException(ErrorCode.TYPE_NOT_FOUND));
        log.info("TypeWebService Response: " + response.toString());
        return response.toTypeDomain();
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
    static class TypeModel {

        private String name;
        private MoveDamageModel moveDamageClass;

        private Type toTypeDomain() {
            return Type.builder()
                    .name(name)
                    .moveDamageClass(moveDamageClass.getName())
                    .build();
        }
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
    static class MoveDamageModel {
        private String name;
    }
}
