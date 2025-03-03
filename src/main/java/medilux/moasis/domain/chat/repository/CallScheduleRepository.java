package medilux.moasis.domain.chat.repository;

import medilux.moasis.domain.chat.domain.CallSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CallScheduleRepository extends JpaRepository<CallSchedule, Long> {
    // 필요 시 사용자 ID로 검색, 날짜 범위 검색 등 추가
}
