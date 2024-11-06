// TranslationService.java
package service.exerciseservice.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class TranslationService {

    @Value("${deepl.api.key}")
    private String apiKey;

    private final String API_URL = "https://api-free.deepl.com/v2/translate";

    public String translateToKorean(String text) {

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "DeepL-Auth-Key " + apiKey);

        // DeepL API 요청 형식에 맞게 수정
        Map<String, Object> requestMap = Map.of(
                "text", List.of(text),
                "target_lang", "KO",
                "source_lang", "EN"  // 원본 언어를 명시적으로 지정
        );

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestMap, headers);

        try {
            log.debug("Sending request to DeepL API...");
            ResponseEntity<DeepLResponse> response = restTemplate.postForEntity(
                    API_URL,
                    request,
                    DeepLResponse.class
            );

            log.debug("Received response from DeepL API. Status code: {}", response.getStatusCode());

            if (response.getBody() != null && !response.getBody().getTranslations().isEmpty()) {
                String translatedText = response.getBody().getTranslations().get(0).getText();
                log.info("Translation successful. Translated text length: {}", translatedText.length());
                return translatedText;
            }

            log.warn("Translation failed: Empty response from DeepL");
            return text;
        } catch (Exception e) {
            log.error("Translation failed", e);
            return text;
        }
    }

    @Data
    private static class DeepLResponse {
        @JsonProperty("translations")
        private List<Translation> translations;
    }

    @Data
    private static class Translation {
        @JsonProperty("text")
        private String text;
    }
}