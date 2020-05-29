package com.pricehunter.core.adapter.controller;

import com.pricehunter.core.application.port.in.CreatePokemonCommand;
import com.pricehunter.core.application.port.in.CreateProductCommand;
import com.pricehunter.core.domain.Pokemon;
import com.pricehunter.core.domain.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/products")
@Slf4j
public final class ProductControllerAdapter {

    private CreateProductCommand createProductCommand;

    public ProductControllerAdapter(CreateProductCommand createProductCommand) {
        this.createProductCommand = createProductCommand;
    }

    @PostMapping
    public Long createPokemon(@RequestBody Product product) {
        CreateProductCommand.Command command = CreateProductCommand.Command.builder()
                .product(product)
                .build();
        return createProductCommand.create(command);
    }

}

