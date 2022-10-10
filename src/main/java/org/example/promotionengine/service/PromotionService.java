package org.example.promotionengine.service;

import org.example.promotionengine.dto.Cart;

import javax.validation.constraints.NotNull;

public interface PromotionService {

    Integer evaluatePriceByApplyingGroupPromotions(@NotNull Cart cart);

    Integer evaluatePriceByApplyingIndividualPromotions(@NotNull Cart cart);
}
