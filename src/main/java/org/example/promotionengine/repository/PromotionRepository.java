package org.example.promotionengine.repository;

import org.example.promotionengine.domain.Promotion;

import java.util.List;

public interface PromotionRepository {

    List<Promotion> findAllGroupPromotions();
}
