package com.example.promotionengine.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;
import javax.validation.constraints.NotEmpty;
import java.util.Map;
import java.util.Objects;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PromotionCreateRequest {

    @ApiModelProperty(
            value = "Promotion Price",
            example = "20",
            dataType = "Integer"
    )
    @Nullable
    private Integer price;

    @ApiModelProperty(
            value = "Unit Price Percentage",
            example = "50",
            dataType = "Integer",
            notes = "Value should be between [1, 100]"
    )
    @Nullable
    private Integer unitPricePercentage;

    @ApiModelProperty(
            value = "Units by SkuId Map",
            example = "{ \"A\": 1, \"B\": 2 }",
            dataType = "Map[String,Integer]"
    )
    @NotEmpty
    private Map<SkuId, Integer> unitsBySkuId;

    public void isValid() {
        if (Objects.isNull(price) && Objects.isNull(unitPricePercentage)) {
            throw new IllegalArgumentException("Price & UnitPricePercentage cannot be null together");
        } else if (Objects.isNull(price) && (unitPricePercentage < 1 || unitPricePercentage > 100)) {
            throw new IllegalArgumentException("UnitPricePercentage should be in the range (1, 100) both inclusive");
        }
    }
}
