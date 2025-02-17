package medilux.moasis.domain.gpt.service;

import medilux.moasis.domain.gpt.dto.ChatRequest;
import medilux.moasis.domain.gpt.prompt.PromptTemplates;
import org.springframework.stereotype.Service;

@Service
public class PromptService {

    /**
     * 사용자 대화 내용과 미리 정의된 템플릿(대화진행 프롬포트 및 요약 프롬포트)을 결합하여 전체 프롬포트를 생성합니다.
     */
    public String buildFullPrompt(ChatRequest request) {
        String userConversation = request.getConversation();

        // 템플릿은 PromptTemplates 클래스에서 관리합니다.
        String conversationPrompt = PromptTemplates.CONVERSATION_PROMPT;
        String summaryPrompt = PromptTemplates.SUMMARY_PROMPT;

        // 필요에 따라 다른 프롬포트(예: 대화요약프롬포트, 대화진행프롬포트)도 추가할 수 있습니다.
        return conversationPrompt + "\n\n" + summaryPrompt + "\n\n" + "대화내용:\n" + userConversation;
    }
}
