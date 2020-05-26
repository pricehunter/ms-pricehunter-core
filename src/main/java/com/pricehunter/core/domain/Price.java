package com.pricehunter.core.domain;

import lombok.Builder;
import lombok.Data;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Value
@Builder
public class Price {
    Long id;
    Double value;
    LocalDateTime date;

}
