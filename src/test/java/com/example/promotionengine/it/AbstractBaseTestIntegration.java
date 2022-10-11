package com.example.promotionengine.it;

import com.example.promotionengine.dto.CartCheckoutInformation;
import com.example.promotionengine.dto.PromotionCreateRequest;
import com.example.promotionengine.dto.PromotionInformation;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.example.promotionengine.utility.PromotionEngineTestUtility.getMapper;
import static com.example.promotionengine.utility.PromotionEngineTestUtility.readEvalFile;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public abstract class AbstractBaseTestIntegration {

    @Autowired
    protected MockMvc mockMvc;

    private AutoCloseable mockAutoCloseable;

    @BeforeEach
    void init() {
        this.mockAutoCloseable = MockitoAnnotations.openMocks(this);
    }

    @SneakyThrows
    void e2e_PromotionEngineFlow_Test() {
        final String CART_URL = "/v1/cart";
        // Add Promotions
        {
            final var addPromotionRequests = getMapper().readValue(readEvalFile(
                    "test-json/promotion/add-promotion-request.json"), new TypeReference<List<PromotionCreateRequest>>() {
            });
            addPromotionRequests.forEach(this::addPromotionAndAssert);
        }
        // Validate Checkout prices...
        {
            // Test Scenario 1
            final var result = mockMvc.perform(post(CART_URL + "/checkout")
                            .content(readEvalFile("test-json/cart/compute-cart-checkout-price-scenario1.json"))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().is2xxSuccessful())
                    .andReturn();
            Assertions.assertNotNull(result);
            Assertions.assertNotNull(result.getResponse());
            Assertions.assertNotNull(result.getResponse().getContentAsString());
            final var cartCheckoutInformation = getMapper().readValue(result.getResponse().getContentAsString(),
                    CartCheckoutInformation.class);
            Assertions.assertEquals(100, cartCheckoutInformation.getCheckoutPrice());
        }
        {
            // Test Scenario 2
            final var result = mockMvc.perform(post(CART_URL + "/checkout")
                            .content(readEvalFile("test-json/cart/compute-cart-checkout-price-scenario2.json"))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().is2xxSuccessful())
                    .andReturn();
            Assertions.assertNotNull(result);
            Assertions.assertNotNull(result.getResponse());
            Assertions.assertNotNull(result.getResponse().getContentAsString());
            final var cartCheckoutInformation = getMapper().readValue(result.getResponse().getContentAsString(),
                    CartCheckoutInformation.class);
            Assertions.assertEquals(370, cartCheckoutInformation.getCheckoutPrice());
        }
        {
            // Test Scenario 3
            final var result = mockMvc.perform(post(CART_URL + "/checkout")
                            .content(readEvalFile("test-json/cart/compute-cart-checkout-price-scenario3.json"))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().is2xxSuccessful())
                    .andReturn();
            Assertions.assertNotNull(result);
            Assertions.assertNotNull(result.getResponse());
            Assertions.assertNotNull(result.getResponse().getContentAsString());
            final var cartCheckoutInformation = getMapper().readValue(result.getResponse().getContentAsString(),
                    CartCheckoutInformation.class);
            Assertions.assertEquals(280, cartCheckoutInformation.getCheckoutPrice());
        }

    }

    @SneakyThrows
    protected void addPromotionAndAssert(final PromotionCreateRequest createRequest) {
        final String PROMOTIONS_URL = "/v1/promotion";
        final var result = mockMvc.perform(post(PROMOTIONS_URL)
                        .content(buildContent(createRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getResponse());
        Assertions.assertNotNull(result.getResponse().getContentAsString());
        final var promotionInformation = getMapper().readValue(result.getResponse().getContentAsString(),
                PromotionInformation.class);
        Assertions.assertEquals(createRequest.getPrice(), promotionInformation.getPrice());
        Assertions.assertTrue(promotionInformation.getUnitsBySkuId().keySet().stream()
                .allMatch(skuId -> createRequest.getUnitsBySkuId().containsKey(skuId)));
    }

    @SneakyThrows
    protected String buildContent(final Object data) {
        return getMapper().writeValueAsString(data);
    }


    @AfterEach
    @SneakyThrows
    void tearDown() {
        this.mockAutoCloseable.close();
    }
}
