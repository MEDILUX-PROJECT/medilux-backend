package medilux.moasis.domain.chat.domain;

import jakarta.persistence.*;
import lombok.*;
import medilux.moasis.domain.login.domain.User;

import java.time.LocalDateTime;

/**
 * 실제 챗봇과 사용자가 주고받은 메시지 로그
 */
@Entity
@Table(name = "chat_message")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    private Long messageId;

    @Enumerated(EnumType.STRING)
    @Column(name = "sender", nullable = false)
    private Sender sender;  // USER or BOT

    @Lob
    @Column(name = "message_content", nullable = false)
    private String messageContent; // 텍스트

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // 어떤 사용자가 보낸 메시지인지
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // 어떤 통화 세션(=call_log)과 연관된 메시지인지
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "call_log_id")
    private ChatSession chatSession;
}
