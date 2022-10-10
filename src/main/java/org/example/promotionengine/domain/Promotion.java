package org.example.promotionengine.domain;

import lombok.Builder;
import lombok.Getter;
import org.example.promotionengine.dto.SkuId;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Map;
import java.util.UUID;

@Getter
@Builder
public class Promotion {

    private UUID id;

    @Min(1)
    @NotNull
    private Integer price;

    @NotEmpty
    private Map<SkuId, Integer> unitsBySkuId;

    public boolean isGroupPromotion() {
        return unitsBySkuId.size() > 1;
    }

    public SkuId retrieveFirstSkuId() {
        return unitsBySkuId.keySet().iterator().next();
    }

    public Integer retrieveFirstSkuIdUnits() {
        return unitsBySkuId.values().iterator().next();
    }
}
