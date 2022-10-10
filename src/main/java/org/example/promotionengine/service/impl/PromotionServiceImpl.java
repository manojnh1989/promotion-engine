package org.example.promotionengine.service.impl;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.example.promotionengine.domain.Promotion;
import org.example.promotionengine.dto.Cart;
import org.example.promotionengine.dto.SkuId;
import org.example.promotionengine.repository.PromotionRepository;
import org.example.promotionengine.service.PromotionService;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@AllArgsConstructor
public class PromotionServiceImpl implements PromotionService {

    private final PromotionRepository promotionRepository;

    @Override
    public Integer evaluatePriceByApplyingGroupPromotions(@NotNull Cart cart) {
        final var groupPromotions = promotionRepository.findAllGroupPromotions();
        final var totalPrice = new AtomicInteger(0);
        groupPromotions.forEach(promotion -> applyGroupPromotion(promotion, cart, totalPrice));
        return totalPrice.get();
    }

    @Override
    public Integer evaluatePriceByApplyingIndividualPromotions(Cart cart) {
        return null;
    }

    private static void applyGroupPromotion(final Promotion promotion, final Cart cart, final AtomicInteger totalPrice) {
        final var minCountByValidFlag = validateIfGroupPromotionApplicableAndReturnCount(promotion, cart);
        if(!minCountByValidFlag.getKey()) {
            return;
        }
        totalPrice.addAndGet(promotion.getPrice() * minCountByValidFlag.getValue());
        cart.removeUnitsByCount(promotion.getUnitsBySku(), minCountByValidFlag.getValue());
    }

    private static Pair<Boolean, Integer> validateIfGroupPromotionApplicableAndReturnCount(final Promotion promotion, final Cart cart) {
        boolean isValid = true;
        int minCount = Integer.MAX_VALUE;
        for (Map.Entry<SkuId, Integer> entry : promotion.getUnitsBySku().entrySet()) {
            if (!cart.containsSkuIdAndUnits(entry)) {
                isValid = false;
                break;
            }
            minCount = Math.min(minCount, ( cart.getUnitsBySkuId(entry.getKey()) / entry.getValue() ));
        }
        return Pair.of(isValid, minCount);
    }
}
