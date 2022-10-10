package org.example.promotionengine.dto;

import lombok.Builder;
import lombok.Value;

import java.util.Map;
import java.util.UUID;

@Value
@Builder
public class PromotionInformation {

    UUID id;

    Integer price;

    Map<SkuId, Integer> unitsBySkuId;
}
