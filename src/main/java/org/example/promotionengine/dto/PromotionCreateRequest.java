package org.example.promotionengine.dto;

import lombok.Builder;
import lombok.Value;
import org.springframework.lang.Nullable;
import javax.validation.constraints.NotEmpty;
import java.util.Map;
import java.util.Objects;

@Value
@Builder
public class PromotionCreateRequest {

    @Nullable
    Integer price;

    @Nullable
    Integer unitPricePercentage;

    @NotEmpty
    Map<SkuId, Integer> unitsBySkuId;

    public void isValid() {
        if (Objects.isNull(price) && Objects.isNull(unitPricePercentage)) {
            throw new IllegalArgumentException("Price & UnitPricePercentage cannot be null together");
        } else if (Objects.isNull(price) && (unitPricePercentage < 1 || unitPricePercentage > 100)) {
            throw new IllegalArgumentException("UnitPricePercentage should be in the range (1, 100) both inclusive");
        }
    }
}
