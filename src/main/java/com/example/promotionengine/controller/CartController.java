package com.example.promotionengine.controller;

import com.example.promotionengine.service.CartService;
import lombok.AllArgsConstructor;
import com.example.promotionengine.dto.CartCheckoutInformation;
import com.example.promotionengine.dto.CartInformation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Validated
@RestController
@AllArgsConstructor
@RequestMapping("/v1/cart")
public class CartController {

    private final CartService cartService;

    @PostMapping("/checkout")
    public CartCheckoutInformation performCheckout(@Valid @RequestBody CartInformation cartInformation) {
        // Compute checkout price...
        final var checkoutPrice = cartService.computeCartCheckoutPrice(cartInformation);
        // Do other checkout actions... ?? <Not in scope>
        return CartCheckoutInformation.builder().checkoutPrice(checkoutPrice).unitsBySkuId(cartInformation.getUnitsBySkuId())
                .build();
    }
}
