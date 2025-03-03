package medilux.moasis.domain.chat.repository;

import medilux.moasis.domain.chat.domain.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    // 사용자별 메시지 조회
    List<ChatMessage> findByUserIdOrderByCreatedAtAsc(Long userId);

    // 세션별 메시지 조회
    List<ChatMessage> findByChatSession_CallLogIdOrderByCreatedAtAsc(Long callLogId);
}
