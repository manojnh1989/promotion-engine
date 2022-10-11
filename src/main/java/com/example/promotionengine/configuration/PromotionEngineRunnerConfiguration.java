package com.example.promotionengine.configuration;

import com.example.promotionengine.constants.PromotionEngineConstants;
import com.example.promotionengine.domain.Promotion;
import com.example.promotionengine.service.CartService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import com.example.promotionengine.dto.CartInformation;
import com.example.promotionengine.dto.SkuId;
import com.example.promotionengine.repository.RemotePromotionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Slf4j
@Configuration
@AllArgsConstructor
@Profile(PromotionEngineConstants.TEST_RUN_PROFILE)
public class PromotionEngineRunnerConfiguration {

    private final CartService cartService;

    @Bean
    @Profile(PromotionEngineConstants.LOCAL_PROFILE)
    public CommandLineRunner commandLineRunnerLocal() {
        return (args) -> {
            log.info("computingCartCheckoutPrice profile:{} and price:{}", PromotionEngineConstants.LOCAL_PROFILE,
                    cartService.computeCartCheckoutPrice(CartInformation.builder().unitsBySkuId(PromotionEngineConstants.TestData.TEST_UNITS_BY_SKU_ID).build()));
        };
    }

    @Bean
    @Transactional
    @Profile(PromotionEngineConstants.REMOTE_PROFILE)
    public CommandLineRunner commandLineRunnerRemote(final RemotePromotionRepository promotionRepository) {
        return (args) -> {

            // Persist data into remote data-source for promotions
            for (Pair<Integer, Map<SkuId, Integer>> pair: PromotionEngineConstants.TestData.TEST_PROMOTIONS) {
                final var promotion = Promotion.builder().price(pair.getKey()).unitsBySkuId(pair.getValue()).build();
                promotion.addPromotionSkuDetail(pair.getValue());
                promotionRepository.save(promotion);
            }

            log.info("computingCartCheckoutPrice profile:{} and price:{}", PromotionEngineConstants.REMOTE_PROFILE,
                    cartService.computeCartCheckoutPrice(CartInformation.builder().unitsBySkuId(PromotionEngineConstants.TestData.TEST_UNITS_BY_SKU_ID).build()));
        };
    }
}
