package com.example.promotionengine.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
