package org.example.promotionengine.repository.impl;

import org.example.promotionengine.domain.Promotion;
import org.example.promotionengine.dto.SkuId;
import org.example.promotionengine.repository.PromotionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.springframework.validation.annotation.Validated;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Profile("local")
@Repository
@Validated
public class LocalPromotionRepository implements PromotionRepository {

    @Autowired(required = false)
    private List<Promotion> promotions;

    @PostConstruct
    public void init() {
        // In-memory promotions...
        this.promotions = new ArrayList<>();
        this.promotions.add(Promotion.builder()
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
    public List<Promotion> findAllPromotions() {
        return this.promotions;
    }
}
