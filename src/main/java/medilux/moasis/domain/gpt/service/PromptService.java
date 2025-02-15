package medilux.moasis.domain.gpt.service;

import medilux.moasis.domain.gpt.dto.ChatRequest;
import medilux.moasis.domain.gpt.prompt.PromptTemplates;
import org.springframework.stereotype.Service;

@Service
public class PromptService {

    public String buildFullPrompt(ChatRequest request) {
        String userConversation = request.getConversation();

        String conversationPrompt = PromptTemplates.CONVERSATION_PROMPT;
        String summaryPrompt = PromptTemplates.SUMMARY_PROMPT;

        return conversationPrompt + "\n\n" + summaryPrompt + "\n\n" + "대화내용:\n" + userConversation;
    }
}

