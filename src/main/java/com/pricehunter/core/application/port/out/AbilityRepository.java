package com.pricehunter.core.application.port.out;

import com.pricehunter.core.domain.Ability;

public interface AbilityRepository {

    Ability getAbility(String name);
}
