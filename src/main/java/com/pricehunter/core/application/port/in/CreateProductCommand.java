package com.pricehunter.core.application.port.in;

import com.pricehunter.core.domain.Product;
import lombok.Builder;
import lombok.Value;

import java.util.UUID;

public interface CreateProductCommand {

    Long create(Command command);

    @Value
    @Builder
    class Command {
       Product product;
    }
}
