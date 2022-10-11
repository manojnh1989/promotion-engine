package com.example.promotionengine.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartCheckoutInformation {

    private Integer checkoutPrice;

    private Map<SkuId, Integer> unitsBySkuId;
}
