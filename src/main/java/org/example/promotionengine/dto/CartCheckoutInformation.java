package org.example.promotionengine.dto;

import lombok.Builder;
import lombok.Value;

import java.util.Map;

@Value
@Builder
public class CartCheckoutInformation {

    Integer checkoutPrice;

    Map<SkuId, Integer> unitsBySkuId;
}
