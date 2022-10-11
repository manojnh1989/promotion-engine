package com.example.promotionengine.utility;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@UtilityClass
@Slf4j
public class PromotionEngineTestUtility {

    public static ObjectMapper getMapper() {
        return JacksonMapper.MAPPER;
    }

    public static boolean assertValueObject(final String expectedFileName, final Object actualObject) {
        try {
            final String expected = readEvalFile(expectedFileName);
            // convert the entity classes into JSON string
            final String actual = getMapper()
                    .writerWithDefaultPrettyPrinter()
                    .writeValueAsString(actualObject);
            if (log.isTraceEnabled()) {
                log.trace("Compare content of expectedFileName:{} with actual:{}", expectedFileName, actual);
            }
            JSONAssert.assertEquals(expected, actual, JSONCompareMode.LENIENT);
        } catch (JsonProcessingException | JSONException e) {
            throw new RuntimeException(e);
        }
        return true;
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
