package com.example.promotionengine.dto;

import io.swagger.annotations.ApiModelProperty;
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

    @ApiModelProperty(
            value = "Unique Identifier",
            example = "f13bec53-c8a3-43f6-a1d7-7faa62296530",
            dataType = "UUID"
    )
    private UUID id;

    @ApiModelProperty(
            value = "Promotion Price",
            example = "10",
            dataType = "Integer"
    )
    private Integer price;

    @ApiModelProperty(
            value = "Units by SkuId Map",
            example = "{ \"A\": 1, \"B\": 2 }",
            dataType = "Map[String,Integer]"
    )
    private Map<SkuId, Integer> unitsBySkuId;
}
