package medilux.moasis.domain.chat.domain;

import jakarta.persistence.*;
import lombok.*;
import medilux.moasis.domain.login.domain.User;

import java.time.LocalDateTime;

/**
 * 챗봇과 연결된 '통화(세션) 로그' 보관
 */
@Entity
@Table(name = "chat_session")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ChatSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "call_log_id")
    private Long callLogId;

    @Column(name = "call_start_time")
    private LocalDateTime callStartTime;

    @Column(name = "call_end_time")
    private LocalDateTime callEndTime;

    @Column(name = "call_duration")
    private Integer callDuration; // 통화시간(초)

    @Enumerated(EnumType.STRING)
    @Column(name = "call_status")
    private CallStatus callStatus; // CONNECTED, MISSED, CANCELLED, FAILED 등

    // 어떤 스케줄에 의해 발생한 콜이었는지
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id")
    private CallSchedule schedule;

    // 누구(어느 사용자)와의 콜인지
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
