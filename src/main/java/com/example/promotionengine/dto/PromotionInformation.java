package com.example.promotionengine.dto;

import lombok.*;

import java.util.Map;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PromotionInformation {

    private UUID id;

    private Integer price;

    private Map<SkuId, Integer> unitsBySkuId;
}
