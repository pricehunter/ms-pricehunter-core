package com.pricehunter.core.adapter.controller.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.pricehunter.core.domain.Price;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Value
@Builder
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class PriceRest {

    Long id;
    Double value;
    LocalDateTime date;

    public static Set<PriceRest> toRest(Set<Price> priceList) {
        return priceList.parallelStream().map(PriceRest::toRest).collect(Collectors.toSet());
    }

    public static PriceRest toRest(Price price) {
        return PriceRest
                .builder()
                .id(price.getId())
                .date(price.getDate())
                .value(price.getValue())
                .build();
    }
}
