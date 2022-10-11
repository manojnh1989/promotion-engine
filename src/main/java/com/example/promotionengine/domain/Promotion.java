package com.example.promotionengine.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.example.promotionengine.dto.SkuId;
import org.springframework.util.CollectionUtils;

import javax.persistence.Id;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.CascadeType;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Getter
@Builder
@Entity
@NoArgsConstructor
public class Promotion {

    @Id
    @NotNull
    private UUID id;

    @Min(1)
    @NotNull
    private Integer price;

    @OneToMany(mappedBy = "promotion", cascade = CascadeType.ALL)
    private List<PromotionSkuDetail> promotionSkuDetails;

    private transient Map<SkuId, Integer> unitsBySkuId;

    public Promotion(final UUID id, final Integer price, final List<PromotionSkuDetail> promotionSkuDetails,
                     final Map<SkuId, Integer> unitsBySkuId) {
        this.id = Objects.isNull(id) ? UUID.randomUUID() : id;
        this.price = price;
        this.unitsBySkuId = CollectionUtils.isEmpty(unitsBySkuId) ? getUnitsBySkuId(promotionSkuDetails) : unitsBySkuId;
    }

    public Map<SkuId, Integer> getUnitsBySkuId(final List<PromotionSkuDetail> promotionSkuDetails) {
        if (!CollectionUtils.isEmpty(promotionSkuDetails)) {
            return promotionSkuDetails.stream().collect(Collectors.toMap(PromotionSkuDetail::getSkuId,
                    PromotionSkuDetail::getUnits));
        }
        return Map.of();
    }

    public List<PromotionSkuDetail> getPromotionSkuDetails(final Map<SkuId, Integer> unitsBySkuId) {
        if (!CollectionUtils.isEmpty(unitsBySkuId)) {
            return unitsBySkuId.entrySet().stream()
                    .map(entry -> PromotionSkuDetail.builder().id(UUID.randomUUID()).skuId(entry.getKey())
                            .units(entry.getValue()).build())
                    .collect(Collectors.toList());
        }
        return List.of();
    }

    public boolean isGroupPromotion() {
        return getUnitsBySkuId().size() > 1;
    }

    public SkuId retrieveFirstSkuId() {
        return getUnitsBySkuId().keySet().iterator().next();
    }

    public Integer retrieveFirstSkuIdUnits() {
        return getUnitsBySkuId().values().iterator().next();
    }

    public void addPromotionSkuDetail(final Map<SkuId, Integer> unitsBySkuId) {
        if (Objects.isNull(this.promotionSkuDetails)) {
            this.promotionSkuDetails = new ArrayList<>();
        }
        unitsBySkuId.forEach((key, val) -> this.promotionSkuDetails.add(
                PromotionSkuDetail.builder().id(UUID.randomUUID()).promotion(this).skuId(key).units(val).build()));
    }

    public Map<SkuId, Integer> getUnitsBySkuId() {
        return CollectionUtils.isEmpty(unitsBySkuId) ? getUnitsBySkuId(this.promotionSkuDetails) : unitsBySkuId;
    }
}
