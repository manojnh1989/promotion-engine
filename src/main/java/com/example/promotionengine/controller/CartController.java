package com.example.promotionengine.controller;

import com.example.promotionengine.service.CartService;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import com.example.promotionengine.dto.CartCheckoutInformation;
import com.example.promotionengine.dto.CartInformation;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;

@Validated
@RestController
@AllArgsConstructor
@RequestMapping("/v1/cart")
@Api(value = "Cart Management Endpoints", tags = "Cart Management", description = "Cart Management Endpoints")
public class CartController {

    private final CartService cartService;

    @PostMapping(value = "/checkout", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CartCheckoutInformation performCheckout(@Valid @RequestBody CartInformation cartInformation) {
        // Take the copy of SkuId and Units.
        final var unitsBySkuId = new HashMap<>(cartInformation.getUnitsBySkuId());
        // Compute checkout price...
        final var checkoutPrice = cartService.computeCartCheckoutPrice(cartInformation);
        // Do other checkout actions... ?? <Not in scope>
        return CartCheckoutInformation.builder().checkoutPrice(checkoutPrice).unitsBySkuId(unitsBySkuId)
                .build();
    }
}
