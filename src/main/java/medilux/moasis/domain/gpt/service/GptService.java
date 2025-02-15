package medilux.moasis.domain.gpt.service;

import medilux.moasis.config.GptConfig;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class GptService {

    private final GptConfig config;
    private final RestTemplate restTemplate;

    public GptService(GptConfig config) {
        this.config = config;
        this.restTemplate = new RestTemplate();
    }

    public String callGpt(String prompt) throws Exception {
        String url = "https://api.openai.com/v1/chat/completions";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(config.getSecretKey());

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", config.getModel());

        List<Map<String, String>> messages = new ArrayList<>();
        Map<String, String> message = new HashMap<>();
        message.put("role", "user");
        message.put("content", prompt);
        messages.add(message);
        requestBody.put("messages", messages);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            List<Map<String, Object>> choices = (List<Map<String, Object>>) response.getBody().get("choices");
            if (choices != null && !choices.isEmpty()) {
                Map<String, Object> messageMap = (Map<String, Object>) choices.get(0).get("message");
                return (String) messageMap.get("content");
            }
        }
        throw new Exception("Failed to get response from GPT API");
    }
}
