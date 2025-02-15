package medilux.moasis.domain.gpt.controller;

import medilux.moasis.domain.gpt.dto.ChatRequest;
import medilux.moasis.domain.gpt.service.GptService;
import medilux.moasis.domain.gpt.service.PromptService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/gpt")
public class GptController {

    private final GptService gptService;
    private final PromptService promptService;

    public GptController(GptService gptService, PromptService promptService) {
        this.gptService = gptService;
        this.promptService = promptService;
    }

    @PostMapping("/summarize")
    public ResponseEntity<?> summarize(@RequestBody ChatRequest request) {
        String fullPrompt = promptService.buildFullPrompt(request);
        try {
            String gptResponse = gptService.callGpt(fullPrompt);
            return ResponseEntity.ok(gptResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("GPT API 호출 실패: " + e.getMessage());
        }
    }
}
