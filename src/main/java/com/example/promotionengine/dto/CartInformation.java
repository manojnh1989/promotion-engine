package com.example.promotionengine.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.HashMap;
import java.util.Map;

@Getter
@Builder
@NoArgsConstructor
public class CartInformation {

    @NotEmpty
    private Map<SkuId, Integer> unitsBySkuId;

    public CartInformation(final Map<SkuId, Integer> unitsBySkuId) {
        this.unitsBySkuId = new HashMap<>(unitsBySkuId);
    }

    public boolean containsSkuIdAndUnits(final Map.Entry<SkuId, Integer> entries) {
        return unitsBySkuId.containsKey(entries.getKey()) && unitsBySkuId.get(entries.getKey()) >= entries.getValue();
    }

    public Integer getUnitsBySkuId(final SkuId skuId) {
        return unitsBySkuId.get(skuId);
    }

    public void removeUnitsByCount(final Map<SkuId, Integer> entries, final Integer count) {
        entries.forEach((key, value) -> unitsBySkuId.put(key, getUnitsBySkuId(key) - (value * count)));
    }
}
