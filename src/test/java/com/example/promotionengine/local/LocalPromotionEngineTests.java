package com.example.promotionengine.local;

import com.example.promotionengine.PromotionEngineApplication;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ActiveProfiles({"test-run", "local"})
@SpringBootTest(classes = PromotionEngineApplication.class)
class LocalPromotionEngineTests {

    @Test
    void contextLoads() {
    }
}
