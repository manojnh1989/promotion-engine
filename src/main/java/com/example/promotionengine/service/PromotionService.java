package com.example.promotionengine.service;

import com.example.promotionengine.dto.CartInformation;
import com.example.promotionengine.dto.PromotionCreateRequest;
import com.example.promotionengine.dto.PromotionInformation;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public interface PromotionService {

    Integer computePriceByApplyingGroupPromotions(@Valid @NotNull CartInformation cart);

    Integer computePriceByApplyingIndividualPromotions(@Valid @NotNull CartInformation cart);

    PromotionInformation addPromotion(PromotionCreateRequest promotionCreateRequest);
}
