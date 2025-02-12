package medilux.moasis.domain.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FcmNotificationRequest {
    private String to;
    private Notification notification;
    private Object data;
}
