package org.example.promotionengine.domain;

import lombok.Builder;
import lombok.Getter;
import org.example.promotionengine.dto.SkuId;
import org.springframework.util.CollectionUtils;

import javax.persistence.Id;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Builder
@Entity
public class Promotion {

    @Id
    @NotNull
    private UUID id;

    @Min(1)
    @NotNull
    private Integer price;

    @OneToMany(mappedBy = "promotion", fetch = FetchType.EAGER, targetEntity = PromotionSkuDetail.class, cascade = {CascadeType.ALL})
    private List<PromotionSkuDetail> promotionSkuDetails;

    @NotEmpty
    private transient Map<SkuId, Integer> unitsBySkuId;

    public Promotion(final UUID id, final Integer price, final List<PromotionSkuDetail> promotionSkuDetails,
                     final Map<SkuId, Integer> unitsBySkuId) {
        this.id = Objects.isNull(id) ? UUID.randomUUID() : id;
        this.price = price;
        this.unitsBySkuId = CollectionUtils.isEmpty(unitsBySkuId) ? getUnitsBySkuId(promotionSkuDetails) : unitsBySkuId;
        this.promotionSkuDetails = promotionSkuDetails;
    }

    public Map<SkuId, Integer> getUnitsBySkuId(final List<PromotionSkuDetail> promotionSkuDetails) {
        if (!CollectionUtils.isEmpty(promotionSkuDetails)) {
            return promotionSkuDetails.stream().collect(Collectors.toMap(PromotionSkuDetail::getSkuId,
                    PromotionSkuDetail::getUnits));
        }
        return Map.of();
    }

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
