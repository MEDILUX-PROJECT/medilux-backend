package medilux.moasis.domain.notification.service;


import medilux.moasis.domain.notification.dto.FcmNotificationRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class FcmService {

    // application.properties에 등록한 FCM 서버 키와 URL을 주입받습니다.
    @Value("${fcm.server.key}")
    private String fcmServerKey;

    @Value("${fcm.fcm.url}")
    private String fcmUrl;

    private final RestTemplate restTemplate;

    public FcmService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * FCM에 푸시 알림 전송
     * @param fcmRequest FCM 전송 요청 객체 (수신자, 알림, 데이터 등 포함)
     */
    public void sendNotification(FcmNotificationRequest fcmRequest) {
        // HTTP 헤더 설정 : JSON 타입 및 Authorization 헤더에 서버 키 추가
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "key=" + fcmServerKey);

        // 요청 바디와 헤더를 포함한 엔티티 생성
        HttpEntity<FcmNotificationRequest> request = new HttpEntity<>(fcmRequest, headers);

        // FCM 엔드포인트로 POST 요청 전송
        ResponseEntity<String> response = restTemplate.postForEntity(fcmUrl, request, String.class);

        // 응답 상태 확인 (필요에 따라 로그 또는 예외처리)
        if (response.getStatusCode() == HttpStatus.OK) {
            System.out.println("FCM 푸시 알림 전송 성공!");
        } else {
            System.out.println("FCM 푸시 알림 전송 실패, 상태 코드: " + response.getStatusCode());
            System.out.println("응답 메시지: " + response.getBody());
        }
    }
}
