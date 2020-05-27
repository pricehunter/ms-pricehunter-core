package com.pricehunter.core.adapter.controller;

import com.pricehunter.core.adapter.controller.model.PriceRest;
import com.pricehunter.core.application.port.in.ProductPriceHistoryQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@Slf4j
public final class ProductPriceHistoryControllerAdapter {

    private ProductPriceHistoryQuery productPriceHistoryQuery;

    public ProductPriceHistoryControllerAdapter(ProductPriceHistoryQuery productPriceHistoryQuery) {
      this.productPriceHistoryQuery = productPriceHistoryQuery;
    }

    @GetMapping("/{id}/prices")
    public List<PriceRest> listPrices(@PathVariable("id") Long id) {

        log.info("Call to /products/{}/prices", id);
        List<PriceRest> pricesView = PriceRest
          .toRest(this.productPriceHistoryQuery.listByProductId(id));
        log.info("Response of services list prices: {}", pricesView);

        return pricesView;
    }
}

