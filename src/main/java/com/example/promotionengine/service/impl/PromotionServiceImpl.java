package com.example.promotionengine.service.impl;

import com.example.promotionengine.domain.Promotion;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import com.example.promotionengine.dto.CartInformation;
import com.example.promotionengine.dto.PromotionCreateRequest;
import com.example.promotionengine.dto.PromotionInformation;
import com.example.promotionengine.dto.SkuId;
import com.example.promotionengine.repository.PromotionRepository;
import com.example.promotionengine.service.PromotionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class PromotionServiceImpl implements PromotionService {

    private final PromotionRepository promotionRepository;

    @Override
    public Integer computePriceByApplyingGroupPromotions(final CartInformation cart) {
        final var groupPromotions = promotionRepository.findAllGroupPromotions();
        final var totalPrice = new AtomicInteger(0);
        groupPromotions.forEach(promotion -> applyGroupPromotion(promotion, cart, totalPrice));
        return totalPrice.get();
    }

    @Override
    public Integer computePriceByApplyingIndividualPromotions(final CartInformation cart) {
        final var individualPromotionsBySkuId = promotionRepository.findAllIndividualPromotionsBySkuId();
        final var totalPrice = new AtomicInteger(0);
        individualPromotionsBySkuId.forEach((key, val) -> applyIndividualPromotions(key, val, cart, totalPrice));
        return totalPrice.get();
    }

    @Override
    @Transactional
    public PromotionInformation addPromotion(final PromotionCreateRequest promotionCreateRequest) {
        final var promotion = buildPromotion(promotionCreateRequest);
        promotion.addPromotionSkuDetail(promotionCreateRequest.getUnitsBySkuId());
        return buildPromotionInformation(promotionRepository.savePromotion(promotion));
    }

    @Override
    public List<PromotionInformation> getAllPromotions() {
        return promotionRepository.findAllPromotions().stream().map(PromotionServiceImpl::buildPromotionInformation)
                .collect(Collectors.toList());
    }

    private static void applyGroupPromotion(final Promotion promotion, final CartInformation cart, final AtomicInteger totalPrice) {
        final var minCountByValidFlag = validateIfGroupPromotionApplicableAndReturnCount(promotion, cart);
        if(!minCountByValidFlag.getKey()) {
            return;
        }
        log.debug("appliedGroupPromotion:{} for times:{}", promotion.getUnitsBySkuId(), minCountByValidFlag.getValue());
        totalPrice.addAndGet(promotion.getPrice() * minCountByValidFlag.getValue());
        cart.removeUnitsByCount(promotion.getUnitsBySkuId(), minCountByValidFlag.getValue());
    }

    private static void applyIndividualPromotions(final SkuId skuId, final Map<Integer, Promotion> promotionsByUnit,
                                                  final CartInformation cart, final AtomicInteger totalPrice) {
        final var unitGroups = evaluateAllUnitGroupsForMatchingTargetUnits(cart.getUnitsBySkuId(skuId),
                promotionsByUnit.keySet());
        // Validate if unit groups is empty
        if (CollectionUtils.isEmpty(unitGroups)) {
            throw new RuntimeException("Failed in applying individual promotions as `unitGroups` is empty / null");
        }
        if (unitGroups.size() > 1) {
            log.debug("applyIndividualPromotions allUnitGroups:{} for SkuId:{}", unitGroups, skuId);
            final var appliedUnitGroup = unitGroups.get(unitGroups.size() - 1);
            log.debug("applyIndividualPromotions appliedUnitGroup:{} for SkuId:{}", appliedUnitGroup, skuId);
            totalPrice.addAndGet(appliedUnitGroup.stream().map(unit ->
                    {
                        if (!promotionsByUnit.containsKey(unit)) {
                            return skuId.getUnitPrice();
                        }
                        return promotionsByUnit.get(unit).getPrice();
                    }).reduce(0, Integer::sum));
        } else {
            log.debug("applyIndividualPromotions denied, calculating through unit price for SkuId:{} and units:{}", skuId,
                    cart.getUnitsBySkuId(skuId));
            totalPrice.addAndGet(skuId.getUnitPrice() * unitGroups.get(0).size());
        }
        // Resetting the units as `0` as they would be already computed in previous step.
        cart.getUnitsBySkuId().put(skuId, 0);
    }

    private static Promotion buildPromotion(final PromotionCreateRequest request) {
        final var promotionBuilder = Promotion.builder().unitsBySkuId(request.getUnitsBySkuId());
        if (Objects.nonNull(request.getPrice())) {
            return promotionBuilder.price(request.getPrice()).build();
        }
        Objects.requireNonNull(request.getUnitPricePercentage());
        return promotionBuilder.price(request.getUnitsBySkuId().entrySet().stream()
                        .map(entry ->
                                ((int) (entry.getKey().getUnitPrice() * ( request.getUnitPricePercentage() / (double) 100 )))
                                        * entry.getValue())
                        .reduce(0, Integer::sum)).build();
    }

    private static PromotionInformation buildPromotionInformation(final Promotion promotion) {
        return PromotionInformation.builder().price(promotion.getPrice()).id(promotion.getId()).unitsBySkuId(promotion.getUnitsBySkuId())
                .build();
    }

    private static List<List<Integer>> evaluateAllUnitGroupsForMatchingTargetUnits(final Integer targetUnits,
                                                                                   final Collection<Integer> promotionUnits) {

        final List<List<Integer>> unitGroups = new ArrayList<>();
        final var promotionUnitsCopy = new ArrayList<>(promotionUnits);
        // Adding the unit `1` to evaluate cases where 1 has be added to reach target...
        // For ex : In case of [A of 3] to reach 5, the group [1, 1, 3] will be needed.
        promotionUnitsCopy.add(1);
        Collections.sort(promotionUnitsCopy);
        evaluateAllUnitGroupsInRecursionToMatchTarget(unitGroups, targetUnits, new ArrayList<>(), promotionUnitsCopy, 0);
        return unitGroups;
    }

    private static void evaluateAllUnitGroupsInRecursionToMatchTarget(final List<List<Integer>> unitGroups, final Integer targetUnits,
                                                                      final List<Integer> tempUnits, final List<Integer> promotionUnits,
                                                                      int counter) {
        if (counter == promotionUnits.size()) {
            return;
        }

        if (targetUnits == 0) {
            unitGroups.add(new ArrayList<>(tempUnits));
            return;
        }

        if (targetUnits >= promotionUnits.get(counter)) {
            tempUnits.add(promotionUnits.get(counter));
            evaluateAllUnitGroupsInRecursionToMatchTarget(unitGroups, targetUnits - promotionUnits.get(counter),
                    tempUnits, promotionUnits, counter);
            tempUnits.remove(tempUnits.size() - 1);
        }
        evaluateAllUnitGroupsInRecursionToMatchTarget(unitGroups, targetUnits, tempUnits, promotionUnits, ++counter);
    }

    private static Pair<Boolean, Integer> validateIfGroupPromotionApplicableAndReturnCount(final Promotion promotion, final CartInformation cart) {
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
