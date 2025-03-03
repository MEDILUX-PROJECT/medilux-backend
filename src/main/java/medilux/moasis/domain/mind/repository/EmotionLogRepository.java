package medilux.moasis.domain.mind.repository;

import medilux.moasis.domain.mind.domain.EmotionLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmotionLogRepository extends JpaRepository<EmotionLog, Long> {
    List<EmotionLog> findByUserId(Long userId);
}
