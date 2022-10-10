package org.example.promotionengine.controller;

import lombok.AllArgsConstructor;
import org.example.promotionengine.dto.PromotionCreateRequest;
import org.example.promotionengine.dto.PromotionInformation;
import org.example.promotionengine.service.PromotionService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/promotion")
@Validated
@AllArgsConstructor
public class PromotionController {

    private final PromotionService promotionService;

    @PostMapping
    public PromotionInformation addPromotion(@Valid @RequestBody PromotionCreateRequest createRequest) {
        createRequest.isValid();
        return promotionService.addPromotion(createRequest);
    }
}
