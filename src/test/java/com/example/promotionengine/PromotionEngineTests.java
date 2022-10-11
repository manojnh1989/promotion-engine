package com.example.promotionengine;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

class PromotionEngineTests {

	@ActiveProfiles({"test-run", "local"})
	@SpringBootTest(classes = PromotionEngineApplication.class)
	static class LocalContextLoadTest {
		@Test
		void contextLoads() {
		}
	}

	@ActiveProfiles({"test-run", "remote"})
	@SpringBootTest(classes = PromotionEngineApplication.class)
	static class RemoteContextLoadTest {
		@Test
		void contextLoads() {
		}
	}
}
