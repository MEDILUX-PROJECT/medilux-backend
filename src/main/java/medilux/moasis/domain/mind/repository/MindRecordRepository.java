package medilux.moasis.domain.mind.repository;

import medilux.moasis.domain.mind.domain.MindRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MindRecordRepository extends JpaRepository<MindRecord, Long> {
    // 예: 기간별, 사용자별 조회 메서드 필요 시
}
