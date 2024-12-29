package medilux.moasis.domain.login.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import medilux.moasis.domain.login.service.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/login/admin")
@RequiredArgsConstructor
public class AdminController {

    private final JwtService jwtService;


    @GetMapping
    public ResponseEntity<String> sayHello() {
        return ResponseEntity.ok("Hi Admin");
    }


    @GetMapping("/userId")
    public ResponseEntity<String> getUserId(@RequestHeader("Authorization") String authToken) {

        log.info("start!\n");
        // JWT 토큰에서 "Bearer " 접두어 제거
        String token = authToken.substring(7);

        // JwtService를 이용해 userId 추출
        Long userId = jwtService.extractUserId(token);

        // 로그로 userId 출력
        log.info("Admin access by user ID: {}", userId);

        // 응답 반환
        return ResponseEntity.ok("Hi Admin, your user ID is: " + userId);
    }
}
