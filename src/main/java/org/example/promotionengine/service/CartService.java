package org.example.promotionengine.service;

import org.example.promotionengine.dto.Cart;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public interface CartService {
    Integer computeCartCheckoutPrice(@Valid @NotNull Cart cart);
}
