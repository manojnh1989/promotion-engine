package com.example.promotionengine.repository;

import com.example.promotionengine.domain.Promotion;
import com.example.promotionengine.dto.SkuId;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public interface PromotionRepository {

    List<Promotion> findAllPromotions();

    Promotion savePromotion(@Valid Promotion promotion);

    @Valid
    default List<Promotion> findAllGroupPromotions() {
        return findAllPromotions().stream().filter(Promotion::isGroupPromotion).collect(Collectors.toList());
    }

    @Valid
    default Map<SkuId, Map<Integer, Promotion>> findAllIndividualPromotionsBySkuId() {
        return findAllPromotions().stream().filter(Predicate.not(Promotion::isGroupPromotion))
                .collect(Collectors.groupingBy(Promotion::retrieveFirstSkuId,
                        Collectors.toMap(Promotion::retrieveFirstSkuIdUnits, Function.identity(), (k1, k2) -> k1)));
    }
}
