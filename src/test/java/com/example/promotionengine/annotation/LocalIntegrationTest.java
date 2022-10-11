package com.example.promotionengine.annotation;

import com.example.promotionengine.PromotionEngineApplication;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.lang.annotation.*;

@Inherited
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@ActiveProfiles({"it", "local"})
@SpringBootTest(classes = PromotionEngineApplication.class)
public @interface LocalIntegrationTest {
}
