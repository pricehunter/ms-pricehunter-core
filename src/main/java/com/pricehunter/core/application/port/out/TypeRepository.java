package com.pricehunter.core.application.port.out;

import com.pricehunter.core.domain.Type;

public interface TypeRepository {

    Type getType(String typeName);
}
