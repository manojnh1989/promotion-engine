package org.example.promotionengine.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.promotionengine.dto.Cart;
import org.example.promotionengine.service.CartService;
import org.example.promotionengine.service.PromotionService;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Slf4j
@Service
@Validated
@AllArgsConstructor
public class CartServiceImpl implements CartService {

    private final PromotionService promotionService;

    @Override
    public Integer computeCartCheckoutPrice(final Cart cart) {
        // Compute Group Promotions
        final var grpPromotionsComputedPrice = promotionService.evaluatePriceByApplyingGroupPromotions(cart);
        log.debug("computeCartCheckoutPrice groupPromotionsComputedPrice:{}", grpPromotionsComputedPrice);
        final var individualPromotionsComputedPrice = promotionService.evaluatePriceByApplyingIndividualPromotions(cart);
        log.debug("computeCartCheckoutPrice individualPromotionsComputedPrice:{}", individualPromotionsComputedPrice);
        return grpPromotionsComputedPrice + individualPromotionsComputedPrice;
    }
}
