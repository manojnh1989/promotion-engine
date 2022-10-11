package com.example.promotionengine.it;

import com.example.promotionengine.annotation.RemoteIntegrationTest;
import com.example.promotionengine.dto.PromotionCreateRequest;
import com.example.promotionengine.dto.PromotionInformation;
import com.example.promotionengine.dto.SkuId;
import com.example.promotionengine.service.PromotionService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;

import java.util.Map;

import static com.example.promotionengine.utility.PromotionEngineTestUtility.getMapper;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RemoteIntegrationTest
public class PromotionIntegrationTestIT extends AbstractBaseTestIntegration {

    @SpyBean
    private PromotionService promotionService;

    @Test
    @SneakyThrows
    void e2e_PromotionFlow_Test() {
        // Success ...
        {
            final int price = 100;
            final var unitsBySkuId = Map.of(SkuId.A, 1);

            Mockito.doReturn(PromotionInformation.builder().price(price).unitsBySkuId(unitsBySkuId).build())
                    .when(promotionService).addPromotion(Mockito.any());
            addPromotionAndAssert(PromotionCreateRequest.builder().price(price).unitsBySkuId(unitsBySkuId).build());
            Mockito.reset(promotionService);
        }
        {
            final int pricePercentage = 50;
            final var unitsBySkuId = Map.of(SkuId.A, 1);

            final String PROMOTIONS_URL = "/v1/promotion";
            final var result = mockMvc.perform(post(PROMOTIONS_URL)
                            .content(buildContent(PromotionCreateRequest.builder().unitPricePercentage(pricePercentage)
                                    .unitsBySkuId(unitsBySkuId).build()))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().is2xxSuccessful())
                    .andReturn();

            Assertions.assertNotNull(result);
            Assertions.assertNotNull(result.getResponse());
            Assertions.assertNotNull(result.getResponse().getContentAsString());
            final var promotionInformation = getMapper().readValue(result.getResponse().getContentAsString(),
                    PromotionInformation.class);
            Assertions.assertEquals((int) (SkuId.A.getUnitPrice() * ( pricePercentage / (double) 100 )), promotionInformation.getPrice());
        }
        // Failure ...
        final String PROMOTIONS_URL = "/v1/promotion";
        {
            mockMvc.perform(post(PROMOTIONS_URL)
                            .content(buildContent(PromotionCreateRequest.builder().build()))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().is4xxClientError())
                    .andReturn();

            mockMvc.perform(post(PROMOTIONS_URL)
                            .content(buildContent(PromotionCreateRequest.builder().unitsBySkuId(Map.of(SkuId.A, 1)).build()))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().is4xxClientError())
                    .andReturn();

            mockMvc.perform(post(PROMOTIONS_URL)
                            .content(buildContent(PromotionCreateRequest.builder().unitPricePercentage(120)
                                    .unitsBySkuId(Map.of(SkuId.A, 1)).build()))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().is4xxClientError())
                    .andReturn();

            mockMvc.perform(post(PROMOTIONS_URL)
                            .content(buildContent(PromotionCreateRequest.builder().unitPricePercentage(0)
                                    .unitsBySkuId(Map.of(SkuId.A, 1)).build()))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().is4xxClientError())
                    .andReturn();
        }
    }
}
