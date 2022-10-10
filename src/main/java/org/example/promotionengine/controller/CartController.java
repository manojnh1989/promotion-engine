package org.example.promotionengine.controller;

import lombok.AllArgsConstructor;
import org.example.promotionengine.dto.CartCheckoutInformation;
import org.example.promotionengine.dto.CartInformation;
import org.example.promotionengine.service.CartService;
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
