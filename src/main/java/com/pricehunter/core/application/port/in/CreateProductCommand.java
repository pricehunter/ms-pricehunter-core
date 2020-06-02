package com.pricehunter.core.application.port.in;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotNull;

public interface CreateProductCommand {

    Long create(Command command);
    @Value
    @Builder
    class Command {
        @NotNull
        String name;
        @NotNull
        String description;
        @NotNull
        Double price;
    }
}
