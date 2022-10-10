package org.example.promotionengine.service;

import org.example.promotionengine.dto.Cart;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public interface PromotionService {

    Integer computePriceByApplyingGroupPromotions(@Valid @NotNull Cart cart);

    Integer computePriceByApplyingIndividualPromotions(@Valid @NotNull Cart cart);
}
