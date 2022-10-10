package org.example.promotionengine.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.Map;

@Data
@Builder
public class Cart {

    @NotEmpty
    private Map<SkuId, Integer> unitsBySkuId;
}
