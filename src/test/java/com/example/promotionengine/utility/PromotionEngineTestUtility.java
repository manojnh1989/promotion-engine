package com.example.promotionengine.utility;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import lombok.experimental.UtilityClass;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@UtilityClass
public class PromotionEngineTestUtility {

    public static ObjectMapper getMapper() {
        return JacksonMapper.MAPPER;
    }

    public static String readEvalFile(final String fileName) {
        final ClassPathResource resource = new ClassPathResource(fileName);
        try (InputStream input = resource.getInputStream()) {
            return IOUtils.toString(input, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static class JacksonMapper {
        private static final ObjectMapper MAPPER = JsonMapper.builder()
                .disable(new DeserializationFeature[]{DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES}).build();
    }
}
