package com.pricehunter.core.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.With;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Builder
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Product {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  private String name;
  private String description;
  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL,
    mappedBy = "product")
  @Builder.Default
  @JsonIgnore
  private Set<Price> prices = Set.of();

  public Product addPrice(Double price) {
    Price priceDm = Price
      .builder()
      .date(LocalDateTime.now())
      .value(price)
      .product(this)
      .build();
    this.setPrices(
      Stream.concat(this.prices.stream(), Stream.of(priceDm)).collect(Collectors.toSet()));
    return this;
  }
}
