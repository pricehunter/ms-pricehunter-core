package com.pricehunter.core.adapter.jpa;

import com.pricehunter.core.domain.Price;
import com.pricehunter.core.domain.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface PriceJPARepository extends CrudRepository<Price, Long> {

    Set<Price> findAllByProductId(Long productId);
}
