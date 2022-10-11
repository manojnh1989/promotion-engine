package com.example.promotionengine.service;

import com.example.promotionengine.dto.CartInformation;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public interface CartService {
    Integer computeCartCheckoutPrice(@Valid @NotNull CartInformation cart);
}
