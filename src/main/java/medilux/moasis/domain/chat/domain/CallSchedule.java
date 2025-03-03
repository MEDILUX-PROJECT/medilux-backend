package medilux.moasis.domain.chat.domain;

import jakarta.persistence.*;
import lombok.*;
import medilux.moasis.domain.login.domain.User;

import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 챗봇과의 상담(콜) 시간을 일정 주기로 예약/설정.
 */
@Entity
@Table(name = "call_schedule")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class CallSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id")
    private Long id;

    @Column(name = "call_time", nullable = false)
    private LocalTime callTime; // 하루 중 콜 실행 시간

    @Enumerated(EnumType.STRING)
    @Column(name = "repeat_cycle", nullable = false)
    private RepeatCycle repeatCycle; // DAILY, WEEKLY, MONTHLY, ONCE

    @Enumerated(EnumType.STRING)
    @Column(name = "call_day")
    private CallDay callDay;   // MON, TUE, WED, THU, FRI, SAT, SUN

    @Column(name = "call_week")
    private Integer callWeek;  // (예: 2 -> 2주차에 콜)

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;   // 스케줄 활성화 여부

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // 연결된 사용자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
