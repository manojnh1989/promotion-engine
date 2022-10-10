package org.example.promotionengine.service.impl;

import org.example.promotionengine.dto.Cart;
import org.example.promotionengine.service.CartService;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Service
@Validated
public class CartServiceImpl implements CartService {

    @Override
    public Integer computeCartCheckoutPrice(@Valid @NotNull Cart cart) {


        return null;
    }
}
