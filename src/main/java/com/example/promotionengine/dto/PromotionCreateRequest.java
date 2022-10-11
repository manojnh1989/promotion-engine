package com.example.promotionengine.dto;

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

    @Nullable
    private Integer price;

    @Nullable
    private Integer unitPricePercentage;

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
