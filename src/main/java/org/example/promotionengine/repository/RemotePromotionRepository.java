package org.example.promotionengine.repository;

import org.example.promotionengine.domain.Promotion;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Profile("remote")
@Repository
public interface RemotePromotionRepository extends JpaRepository<Promotion, UUID>, PromotionRepository {

    @Override
    default List<Promotion> findAllPromotions() {
        return findAll();
    }
}
