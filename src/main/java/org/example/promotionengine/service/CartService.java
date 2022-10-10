package org.example.promotionengine.service;

import org.example.promotionengine.dto.Cart;

import javax.validation.constraints.NotNull;

public interface CartService {

    Integer computeCartCheckoutPrice(@NotNull Cart cart);
}
