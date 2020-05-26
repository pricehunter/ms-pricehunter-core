package com.pricehunter.core.adapter.controller.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.pricehunter.core.domain.Pokemon;
import com.pricehunter.core.domain.Price;
import lombok.Builder;
import lombok.Value;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Value
@Builder
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class PriceRest {

    Long id;
    Double value;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    LocalDateTime date;

    public static List<PriceRest> toRest(List<Price> priceList) {
        return priceList.parallelStream().map(PriceRest::toRest).collect(Collectors.toList());
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
