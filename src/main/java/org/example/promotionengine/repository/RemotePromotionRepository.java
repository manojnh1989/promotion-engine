package org.example.promotionengine.repository;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

@Profile("remote")
@Repository
public interface RemotePromotionRepository extends PromotionRepository {

}
