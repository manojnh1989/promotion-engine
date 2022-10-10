package org.example.promotionengine.service;

import org.example.promotionengine.dto.CartInformation;
import org.example.promotionengine.dto.PromotionCreateRequest;
import org.example.promotionengine.dto.PromotionInformation;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public interface PromotionService {

    Integer computePriceByApplyingGroupPromotions(@Valid @NotNull CartInformation cart);

    Integer computePriceByApplyingIndividualPromotions(@Valid @NotNull CartInformation cart);

    PromotionInformation addPromotion(PromotionCreateRequest promotionCreateRequest);
}
