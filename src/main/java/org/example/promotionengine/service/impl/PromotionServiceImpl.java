package org.example.promotionengine.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.example.promotionengine.domain.Promotion;
import org.example.promotionengine.dto.Cart;
import org.example.promotionengine.dto.SkuId;
import org.example.promotionengine.repository.PromotionRepository;
import org.example.promotionengine.service.PromotionService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@AllArgsConstructor
@Slf4j
public class PromotionServiceImpl implements PromotionService {

    private final PromotionRepository promotionRepository;

    @Override
    public Integer computePriceByApplyingGroupPromotions(final Cart cart) {
        final var groupPromotions = promotionRepository.findAllGroupPromotions();
        final var totalPrice = new AtomicInteger(0);
        groupPromotions.forEach(promotion -> applyGroupPromotion(promotion, cart, totalPrice));
        return totalPrice.get();
    }

    @Override
    public Integer computePriceByApplyingIndividualPromotions(final Cart cart) {
        final var individualPromotionsBySkuId = promotionRepository.findAllIndividualPromotionsBySkuId();
        final var totalPrice = new AtomicInteger(0);
        individualPromotionsBySkuId.forEach((key, val) -> applyIndividualPromotions(key, val, cart, totalPrice));
        return totalPrice.get();
    }

    private static void applyGroupPromotion(final Promotion promotion, final Cart cart, final AtomicInteger totalPrice) {
        final var minCountByValidFlag = validateIfGroupPromotionApplicableAndReturnCount(promotion, cart);
        if(!minCountByValidFlag.getKey()) {
            return;
        }
        totalPrice.addAndGet(promotion.getPrice() * minCountByValidFlag.getValue());
        cart.removeUnitsByCount(promotion.getUnitsBySkuId(), minCountByValidFlag.getValue());
    }

    private static void applyIndividualPromotions(final SkuId skuId, final Map<Integer, Promotion> promotionsByUnit,
                                                  final Cart cart, final AtomicInteger totalPrice) {
        final var unitGroups = evaluateAllUnitGroupsForMatchingTargetUnits(cart.getUnitsBySkuId(skuId),
                promotionsByUnit.keySet());
        if (unitGroups.size() > 1) {
            log.info("applyIndividualPromotions allUnitGroups:{}", unitGroups);
            final var appliedUnitGroup = unitGroups.get(unitGroups.size() - 1);
            log.info("applyIndividualPromotions appliedUnitGroup:{}", appliedUnitGroup);
            totalPrice.addAndGet(appliedUnitGroup.stream().map(unit ->
                    {
                        if (!promotionsByUnit.containsKey(unit)) {
                            return skuId.getUnitPrice();
                        }
                        return promotionsByUnit.get(unit).getPrice();
                    }).reduce(0, Integer::sum));
        } else {
            totalPrice.addAndGet(skuId.getUnitPrice() * unitGroups.get(0).size());
        }
        // Resetting the units as `0`
        cart.getUnitsBySkuId().put(skuId, 0);
    }

    private static List<List<Integer>> evaluateAllUnitGroupsForMatchingTargetUnits(final Integer targetPrice,
                                                                                   final Collection<Integer> promotionPrices) {

        final List<List<Integer>> unitGroups = new ArrayList<>();
        final var promotionPricesCopy = new ArrayList<>(promotionPrices);
        promotionPricesCopy.add(1);
        Collections.sort(promotionPricesCopy);
        evaluateAllUnitGroupsInRecursionToMatchTarget(unitGroups, targetPrice, new ArrayList<>(), promotionPricesCopy, 0);
        return unitGroups;
    }

    private static void evaluateAllUnitGroupsInRecursionToMatchTarget(final List<List<Integer>> unitGroups, final Integer targetPrice,
                                                                      final List<Integer> tempPrices, final List<Integer> promotionPrices,
                                                                      int counter) {
        if (counter == promotionPrices.size()) {
            return;
        }

        if (targetPrice == 0) {
            unitGroups.add(new ArrayList<>(tempPrices));
            return;
        }

        if (targetPrice >= promotionPrices.get(counter)) {
            tempPrices.add(promotionPrices.get(counter));
            evaluateAllUnitGroupsInRecursionToMatchTarget(unitGroups, targetPrice - promotionPrices.get(counter),
                    tempPrices, promotionPrices, counter);
            tempPrices.remove(tempPrices.size() - 1);
        }
        evaluateAllUnitGroupsInRecursionToMatchTarget(unitGroups, targetPrice, tempPrices, promotionPrices, ++counter);
    }

    private static Pair<Boolean, Integer> validateIfGroupPromotionApplicableAndReturnCount(final Promotion promotion, final Cart cart) {
        boolean isValid = true;
        int minCount = Integer.MAX_VALUE;
        for (Map.Entry<SkuId, Integer> entry : promotion.getUnitsBySkuId().entrySet()) {
            if (!cart.containsSkuIdAndUnits(entry)) {
                isValid = false;
                break;
            }
            minCount = Math.min(minCount, ( cart.getUnitsBySkuId(entry.getKey()) / entry.getValue() ));
        }
        return Pair.of(isValid, minCount);
    }
}
