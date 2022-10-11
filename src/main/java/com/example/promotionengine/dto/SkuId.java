package com.example.promotionengine.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SkuId {

    A(50),
    B(30),
    C(20),
    D(15);

    private final Integer unitPrice;
}
