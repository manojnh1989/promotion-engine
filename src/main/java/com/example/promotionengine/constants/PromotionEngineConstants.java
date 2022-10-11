package com.example.promotionengine.constants;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.tuple.Pair;
import com.example.promotionengine.dto.SkuId;

import java.util.List;
import java.util.Map;

@UtilityClass
public class PromotionEngineConstants {

    public static final String LOCAL_PROFILE = "local";
    public static final String REMOTE_PROFILE = "remote";
    public static final String TEST_RUN_PROFILE = "test-run";

    @UtilityClass
    public static class TestData {
        public static final Map<SkuId, Integer> TEST_UNITS_BY_SKU_ID = Map.of(
                SkuId.A, 3,
                SkuId.B, 5,
                SkuId.C, 1,
                SkuId.D, 1
        );

        public static final List<Pair<Integer, Map<SkuId, Integer>>> TEST_PROMOTIONS = List.of(
                Pair.of(130, Map.of(SkuId.A, 3)),
                Pair.of(45, Map.of(SkuId.B, 2)),
                Pair.of(30, Map.of(SkuId.C, 1, SkuId.D, 1))
        );
    }
}
