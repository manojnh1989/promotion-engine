package org.example.promotionengine;

import org.example.promotionengine.dto.Cart;
import org.example.promotionengine.dto.SkuId;
import org.example.promotionengine.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Map;

@SpringBootApplication
public class PromotionEngineApplication {

    @Autowired
    private CartService cartService;

    public static void main(String[] args) {
        SpringApplication.run(PromotionEngineApplication.class, args);
    }

    @Bean
    public CommandLineRunner lineRunner() {
        return (args) -> {
            cartService.computeCartCheckoutPrice(Cart.builder().unitsBySkuId(
                    Map.of(
                            SkuId.A, 1,
                            SkuId.B, 1,
                            SkuId.C, 1
                    )
            ).build());
        };
    }
}
