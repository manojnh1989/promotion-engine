package com.example.promotionengine;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ActiveProfiles({"test-run", "remote"})
@SpringBootTest(classes = PromotionEngineApplication.class)
class PromotionEngineTests {

	@Test
	void contextLoads() {
	}
}
