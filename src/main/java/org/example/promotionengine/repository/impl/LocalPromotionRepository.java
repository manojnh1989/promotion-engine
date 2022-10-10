package org.example.promotionengine.repository.impl;

import org.example.promotionengine.domain.Promotion;
import org.example.promotionengine.dto.SkuId;
import org.example.promotionengine.repository.PromotionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.validation.annotation.Validated;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Repository
@Validated
public class LocalPromotionRepository implements PromotionRepository {

    @Autowired(required = false)
    private List<Promotion> promotions;

    @PostConstruct
    public void init() {
        // In-memory promotions...
        this.promotions = new ArrayList<>();
        this.promotions.add(
                Promotion.builder()
                        .price(130)
                        .unitsBySkuId(Map.of(
                                SkuId.A, 3
                        )).build());
        this.promotions.add(Promotion.builder()
                        .price(45)
                        .unitsBySkuId(Map.of(
                                SkuId.B, 2
                        )).build());
        this.promotions.add(Promotion.builder()
                        .price(30)
                        .unitsBySkuId(Map.of(
                                SkuId.C, 1,
                                SkuId.D, 1
                        )).build());
    }

    @Override
    public List<Promotion> findAllGroupPromotions() {
        return this.promotions.stream().filter(Promotion::isGroupPromotion).collect(Collectors.toList());
    }

    @Override
    public Map<SkuId, Map<Integer, Promotion>> findAllIndividualPromotionsBySkuId() {
        return this.promotions.stream().filter(Predicate.not(Promotion::isGroupPromotion))
                .collect(Collectors.groupingBy(Promotion::retrieveFirstSkuId,
                         Collectors.toMap(Promotion::retrieveFirstSkuIdUnits, Function.identity(), (k1, k2) -> k1)));
    }
}
