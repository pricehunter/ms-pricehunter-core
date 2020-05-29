package com.pricehunter.core.adapter.jpa;

import com.pricehunter.core.domain.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductJPARepository extends CrudRepository<Product, Long> {
}
