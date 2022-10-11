package com.example.promotionengine.controller;

import com.example.promotionengine.dto.PromotionCreateRequest;
import com.example.promotionengine.dto.PromotionInformation;
import com.example.promotionengine.service.PromotionService;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/v1/promotion")
@Validated
@AllArgsConstructor
@Api(value = "Promotion Management Endpoints", tags = "Promotion Management", description = "Promotion Management Endpoints")
public class PromotionController {

    private final PromotionService promotionService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<PromotionInformation> getAllPromotions() {
        return promotionService.getAllPromotions();
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public PromotionInformation addPromotion(@Valid @RequestBody PromotionCreateRequest createRequest) {
        createRequest.isValid();
        return promotionService.addPromotion(createRequest);
    }
}
