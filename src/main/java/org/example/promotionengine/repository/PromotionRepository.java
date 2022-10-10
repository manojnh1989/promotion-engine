package org.example.promotionengine.repository;

import org.example.promotionengine.domain.Promotion;
import org.example.promotionengine.dto.SkuId;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

public interface PromotionRepository {

    @Valid
    List<Promotion> findAllGroupPromotions();

    @Valid
    Map<SkuId, Map<Integer, Promotion>> findAllIndividualPromotionsBySkuId();
}
