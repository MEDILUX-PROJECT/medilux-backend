package medilux.moasis.domain.notification.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import medilux.moasis.domain.notification.dto.FcmNotificationRequest;
import medilux.moasis.domain.notification.service.FcmService;
import medilux.moasis.global.exception.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private FcmService fcmService;

    /**
     * POST /api/notifications/send
     * 요청 본문(JSON) 예시:
     * {
     * "to": "클라이언트_device_token",
     * "notification": {
     * "title": "테스트 알림",
     * "body": "FCM을 통한 푸시 알림 테스트"
     * },
     * "data": {
     * "extraData": "추가 정보"
     * }
     * }
     */
    @PostMapping("/send")
    public BaseResponse<Object> sendNotification(@RequestBody FcmNotificationRequest request) {
        fcmService.sendNotification(request);
        return BaseResponse.<Object>builder()
                    .code(200)
                    .isSuccess(true)
                    .message("FCM 알림 전송 요청 완료")
                    .build();
    }
}
