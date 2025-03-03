package medilux.moasis.domain.chat.repository;

import medilux.moasis.domain.chat.domain.ChatSession;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatSessionRepository extends JpaRepository<ChatSession, Long> {
    // 필요하면 사용자별 세션 목록, 스케줄별 조회 등
}
