package com.pricehunter.core.domain;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class Page<T> {
    private Integer size;
    private Integer page;
    private Integer totalPages;
    private List<T> elements;

}
