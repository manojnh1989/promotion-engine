package org.example.promotionengine.service;

import org.example.promotionengine.dto.Cart;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public interface PromotionService {

    Integer evaluatePriceByApplyingGroupPromotions(@Valid @NotNull Cart cart);

    Integer evaluatePriceByApplyingIndividualPromotions(@Valid @NotNull Cart cart);
}
