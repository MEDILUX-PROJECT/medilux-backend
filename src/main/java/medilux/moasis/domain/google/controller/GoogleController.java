//package medilux.moasis.domain.google.controller;
//
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import medilux.moasis.domain.google.service.GoogleApi;
//import medilux.moasis.domain.login.domain.User;
//import medilux.moasis.domain.login.dto.JwtAuthenticationResponse;
//import medilux.moasis.domain.login.service.AuthenticationService;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
//import java.io.IOException;
//import java.util.Map;
//import java.util.Optional;
//
//@Slf4j
//@Controller
//@RequestMapping("/api/google/auth")
//@RequiredArgsConstructor
//public class GoogleController {
//
//    private final GoogleApi googleApi;
//    private final AuthenticationService authenticationService;
//
//    @Value("${spring.security.oauth2.client.registration.google.client-id}")
//    private String clientId;
//
//    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
//    private String clientSecret;
//
//    @GetMapping("/login")
//    public String loginForm(Model model) {
//        model.addAttribute("googleClientId", clientId);
//        return "googlelogin"; // googlelogin.html 반환
//    }
//
//    @GetMapping("/logout")
//    public String logoutForm() {
//        return "googlelogin"; // 로그아웃 후 다시 로그인 페이지로 이동
//    }
//
//    @GetMapping("/login/oauth2")
//    public ResponseEntity<JwtAuthenticationResponse> loginByGoogle(@RequestParam("code") String code, HttpServletResponse response) throws IOException {
//        // 1. 인가 코드로 액세스 토큰 요청
//        log.info("액세스 토큰 요청 시작");
//        String accessToken = googleApi.getAccessToken(code);
//        log.info("accessToken: " + accessToken);
//
//        // 2. 액세스 토큰으로 사용자 정보 요청
//        log.info("사용자 정보 요청 시작");
//        Map<String, Object> userInfo = googleApi.getUserInfo(accessToken);
//
//        String email = (String) userInfo.get("email");
//        String name = (String) userInfo.get("name");
//
//        log.info("email = " + email);
//        log.info("name = " + name);
//
//        // 3. DB에서 이메일로 사용자 확인 후 회원가입 또는 로그인
//        Optional<User> user = authenticationService.googleSignup(email, name);
//
//        // JWT 발급
//        JwtAuthenticationResponse jwtAuthenticationResponse = authenticationService.googleSignin(user.get().getEmail());
//
//        return ResponseEntity.ok(jwtAuthenticationResponse);
//    }
//
//    // 리디렉션 처리
//    // @GetMapping("/redirect")
//    // public ResponseEntity<?> handleRedirect(...) {
//    //     // 필요시 추가 구현
//    // }
//}
