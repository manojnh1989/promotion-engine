package com.example.promotionengine.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.example.promotionengine.dto.CartInformation;
import com.example.promotionengine.service.CartService;
import com.example.promotionengine.service.PromotionService;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Slf4j
@Service
@Validated
@AllArgsConstructor
public class CartServiceImpl implements CartService {

    private final PromotionService promotionService;

    @Override
    public Integer computeCartCheckoutPrice(final CartInformation cart) {
        // Compute Group Promotions
        final var grpPromotionsComputedPrice = promotionService.computePriceByApplyingGroupPromotions(cart);
        log.debug("computeCartCheckoutPrice groupPromotionsComputedPrice:{}", grpPromotionsComputedPrice);
        // Compute Individual Promotions
        final var individualPromotionsComputedPrice = promotionService.computePriceByApplyingIndividualPromotions(cart);
        log.debug("computeCartCheckoutPrice individualPromotionsComputedPrice:{}", individualPromotionsComputedPrice);
        // Compute Remaining Units
        final var remainingUnitsComputedPrice = computePriceForRemainingUnitsUsingUnitPrices(cart);
        log.debug("computeCartCheckoutPrice remainingUnitsComputedPrice:{}", remainingUnitsComputedPrice);
        return grpPromotionsComputedPrice + individualPromotionsComputedPrice + remainingUnitsComputedPrice;
    }

    private static Integer computePriceForRemainingUnitsUsingUnitPrices(final CartInformation cart) {
        return cart.getUnitsBySkuId().entrySet().stream().filter(e -> e.getValue() > 0)
                .map(e -> e.getKey().getUnitPrice() * e.getValue()).reduce(0, Integer::sum);
    }
}
