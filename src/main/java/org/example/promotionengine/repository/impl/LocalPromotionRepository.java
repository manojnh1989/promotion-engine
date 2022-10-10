package org.example.promotionengine.repository.impl;

import org.example.promotionengine.domain.Promotion;
import org.example.promotionengine.repository.PromotionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.springframework.validation.annotation.Validated;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

import static org.example.promotionengine.constants.PromotionEngineConstants.LOCAL_PROFILE;
import static org.example.promotionengine.constants.PromotionEngineConstants.TestData.TEST_PROMOTIONS;

@Profile(LOCAL_PROFILE)
@Repository
@Validated
public class LocalPromotionRepository implements PromotionRepository {

    @Autowired(required = false)
    private List<Promotion> promotions;

    @PostConstruct
    public void init() {
        promotions = TEST_PROMOTIONS.stream().map(pair ->
                Promotion.builder().price(pair.getKey()).unitsBySkuId(pair.getValue()).build()).collect(Collectors.toList());
    }

    @Override
    public List<Promotion> findAllPromotions() {
        return this.promotions;
    }

    @Override
    public Promotion savePromotion(final Promotion promotion) {
        this.promotions.add(promotion);
        return promotion;
    }
}
