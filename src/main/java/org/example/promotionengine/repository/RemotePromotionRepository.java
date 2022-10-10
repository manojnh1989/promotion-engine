package org.example.promotionengine.repository;

import org.example.promotionengine.domain.Promotion;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

import static org.example.promotionengine.constants.PromotionEngineConstants.REMOTE_PROFILE;

@Profile(REMOTE_PROFILE)
@Repository
public interface RemotePromotionRepository extends JpaRepository<Promotion, UUID>, PromotionRepository {

    @Query(value = "SELECT p FROM Promotion p JOIN FETCH p.promotionSkuDetails")
    List<Promotion> findAllPromotionsAndPromotionSkuDetails();

    @Override
    default List<Promotion> findAllPromotions() {
        return findAllPromotionsAndPromotionSkuDetails();
    }
}
