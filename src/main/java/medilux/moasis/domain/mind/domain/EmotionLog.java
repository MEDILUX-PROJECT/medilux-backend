package medilux.moasis.domain.mind.domain;

import jakarta.persistence.*;
import lombok.*;
import medilux.moasis.domain.chat.domain.ChatSession;
import medilux.moasis.domain.login.domain.User;

import java.time.LocalDateTime;

/**
 * 실시간/일회성으로 기록된 감정로그 (스트레스, 우울, 불안 등 지표)
 */
@Entity
@Table(name = "emotion_log")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class EmotionLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "emotion_log_id")
    private Long emotionLogId;

    @Column(name = "recorded_at")
    private LocalDateTime recordedAt; // 기록 시점

    // (TINYINT)
    @Column(name = "stress_level")
    private Byte stressLevel;

    @Column(name = "sadness_level")
    private Byte sadnessLevel;

    @Column(name = "anxiety_level")
    private Byte anxietyLevel;

    @Column(name = "memo", length = 255)
    private String memo; // 사용자 메모

    // user_id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // chat_session (optional)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "call_log_id")
    private ChatSession chatSession;
}
