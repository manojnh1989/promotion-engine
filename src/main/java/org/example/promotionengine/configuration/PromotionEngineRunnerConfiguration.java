package org.example.promotionengine.configuration;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.example.promotionengine.domain.Promotion;
import org.example.promotionengine.dto.CartInformation;
import org.example.promotionengine.dto.SkuId;
import org.example.promotionengine.repository.RemotePromotionRepository;
import org.example.promotionengine.service.CartService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static org.example.promotionengine.constants.PromotionEngineConstants.*;
import static org.example.promotionengine.constants.PromotionEngineConstants.TestData.TEST_PROMOTIONS;
import static org.example.promotionengine.constants.PromotionEngineConstants.TestData.TEST_UNITS_BY_SKU_ID;

@Slf4j
@Configuration
@AllArgsConstructor
@Profile(TEST_RUN_PROFILE)
public class PromotionEngineRunnerConfiguration {

    private final CartService cartService;

    @Bean
    @Profile(LOCAL_PROFILE)
    public CommandLineRunner commandLineRunnerLocal() {
        return (args) -> {
            log.info("computingCartCheckoutPrice profile:{} and price:{}", LOCAL_PROFILE,
                    cartService.computeCartCheckoutPrice(CartInformation.builder().unitsBySkuId(TEST_UNITS_BY_SKU_ID).build()));
        };
    }

    @Bean
    @Transactional
    @Profile(REMOTE_PROFILE)
    public CommandLineRunner commandLineRunnerRemote(final RemotePromotionRepository promotionRepository) {
        return (args) -> {

            // Persist data into remote data-source for promotions
            for (Pair<Integer, Map<SkuId, Integer>> pair: TEST_PROMOTIONS) {
                final var promotion = Promotion.builder().price(pair.getKey()).unitsBySkuId(pair.getValue()).build();
                promotion.addPromotionSkuDetail(pair.getValue());
                promotionRepository.save(promotion);
            }

            log.info("computingCartCheckoutPrice profile:{} and price:{}", REMOTE_PROFILE,
                    cartService.computeCartCheckoutPrice(CartInformation.builder().unitsBySkuId(TEST_UNITS_BY_SKU_ID).build()));
        };
    }
}
