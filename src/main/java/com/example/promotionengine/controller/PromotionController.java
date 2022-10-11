package com.example.promotionengine.controller;

import com.example.promotionengine.dto.PromotionCreateRequest;
import com.example.promotionengine.dto.PromotionInformation;
import com.example.promotionengine.service.PromotionService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/promotion")
@Validated
@AllArgsConstructor
public class PromotionController {

    private final PromotionService promotionService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PromotionInformation addPromotion(@Valid @RequestBody PromotionCreateRequest createRequest) {
        createRequest.isValid();
        return promotionService.addPromotion(createRequest);
    }
}
