package com.pricehunter.core.adapter.controller;

import com.pricehunter.core.adapter.controller.model.ProductRest;
import com.pricehunter.core.application.port.in.CreateProductCommand;
import com.pricehunter.core.application.port.in.ProductQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/products")
@Slf4j
public final class ProductControllerAdapter {

    private CreateProductCommand createProductCommand;
    private ProductQuery productQuery;

    public ProductControllerAdapter(
      CreateProductCommand createProductCommand,
      ProductQuery productQuery) {
        this.createProductCommand = createProductCommand;
        this.productQuery = productQuery;
    }

    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody CreateProductCommand.Command command, UriComponentsBuilder builder) {
        UriComponents uriComponents =
          builder
            .path("/api/v1/products/{id}")
            .buildAndExpand(createProductCommand.create(command));
        return ResponseEntity.created(uriComponents.toUri()).build();
    }

    @GetMapping(value = "/{id}")
    public ProductRest get(@PathVariable(value = "id") Long id) {
      return ProductRest.toRest(this.productQuery.getById(id));
    }

}

