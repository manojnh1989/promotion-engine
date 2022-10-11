package com.example.promotionengine.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.example.promotionengine.dto.SkuId;

import javax.persistence.Id;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.EnumType;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Getter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class PromotionSkuDetail {

    @Id
    private UUID id;

    @NotNull
    @Enumerated(EnumType.STRING)
    private SkuId skuId;

    @Min(1)
    @NotNull
    private Integer units;

    @ManyToOne
    private Promotion promotion;
}
