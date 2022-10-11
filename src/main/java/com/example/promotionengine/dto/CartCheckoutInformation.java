package com.example.promotionengine.dto;

import io.swagger.annotations.ApiModelProperty;
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

    @ApiModelProperty(
            value = "Checkout Price",
            example = "20",
            dataType = "Integer"
    )
    private Integer checkoutPrice;

    @ApiModelProperty(
            value = "Units by SkuId Map",
            example = "{ \"A\": 1, \"B\": 2 }",
            dataType = "Map[String,Integer]"
    )
    private Map<SkuId, Integer> unitsBySkuId;
}
