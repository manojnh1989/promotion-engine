package org.example.promotionengine.repository.impl;

import org.example.promotionengine.domain.Promotion;
import org.example.promotionengine.repository.PromotionRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LocalPromotionRepositoryImpl implements PromotionRepository {

    @Autowired(required = false)
    private List<Promotion> promotions;

    @PostConstruct
    public void init() {
        // In-memory promotions...
        this.promotions = new ArrayList<>();
    }

    public List<Promotion> findAllGroupPromotions() {
        return this.promotions.stream().filter(Promotion::isGroupPromotion).collect(Collectors.toList());
    }
}
