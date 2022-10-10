package org.example.promotionengine.service;

import org.example.promotionengine.dto.CartInformation;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public interface CartService {
    Integer computeCartCheckoutPrice(@Valid @NotNull CartInformation cart);
}
