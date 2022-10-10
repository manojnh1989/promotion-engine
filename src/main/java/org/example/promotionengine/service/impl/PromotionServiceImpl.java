package org.example.promotionengine.service.impl;

import org.example.promotionengine.domain.Promotion;
import org.example.promotionengine.dto.Cart;
import org.example.promotionengine.service.PromotionService;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;

@Service
public class PromotionServiceImpl implements PromotionService {

    @Override
    public Integer evaluatePriceByApplyingGroupPromotions(@NotNull Cart cart) {




        return null;
    }

    @Override
    public Integer evaluatePriceByApplyingIndividualPromotions(Cart cart) {
        return null;
    }
}
