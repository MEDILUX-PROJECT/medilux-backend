package medilux.moasis.domain.mind.domain;

import jakarta.persistence.*;
import lombok.*;
import medilux.moasis.domain.login.domain.User;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 일정 기간(예: 주간/월간) 감정 추이를 요약하여 보관.
 * 실제 정신과에서 사용하는 요약 템플릿 형태.
 */
@Entity
@Table(name = "mind_record")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class MindRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "record_id")
    private Long recordId;

    @Column(name = "period_start_date", nullable = false)
    private LocalDate periodStartDate; // 분석 기간 시작

    @Column(name = "period_end_date", nullable = false)
    private LocalDate periodEndDate;   // 분석 기간 종료

    @Lob
    @Column(name = "summary_text")
    private String summaryText;        // AI/상담사가 작성한 요약 문구

    @Column(name = "stress_avg_level", precision = 3, scale = 2)
    private BigDecimal stressAvgLevel; // 기간 평균 스트레스 지표

    @Column(name = "cactus_stage")
    private Byte cactusStage;          // 선인장 단계(예: 1~5)

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // user_id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
