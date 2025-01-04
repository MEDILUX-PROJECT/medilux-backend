package medilux.moasis.domain.kakao.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import medilux.moasis.domain.kakao.service.KakaoApi;
import medilux.moasis.domain.login.domain.User;
import medilux.moasis.domain.login.dto.JwtAuthenticationResponse;
import medilux.moasis.domain.login.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/api/kakao/auth")
@RequiredArgsConstructor
public class KakaoController {

    private final KakaoApi kakaoApi;
    private final AuthenticationService authenticationService;

    @Value("${kakao.native_app_key}")
    String nativeAppKey;  // NATIVE_APP_KEY 설정

    @GetMapping("/login")
    public String loginForm(Model model){
        model.addAttribute("kakaoApiKey", kakaoApi.getKakaoApiKey());
        model.addAttribute("redirectUri", kakaoApi.getKakaoRedirectUri());
        return "kakaologin";
    }

    @GetMapping("/logout")
    public String logoutForm(Model model){

        return "kakaologin";
    }

    @GetMapping("/login/oauth2")
    public ResponseEntity<JwtAuthenticationResponse> loginByKakao(@RequestParam("code") String code, HttpServletResponse response) throws IOException {
        // 1. 인가 코드 받기

        // 2. 토큰 받기
        System.out.println("토큰 받기 시작");
        String accessToken = kakaoApi.getAccessToken(code);
        log.info("accessToken: "+ accessToken);

        // 3. 사용자 정보 받기
        System.out.println("사용자 정보 받기 시작");
        Map<String, Object> userInfo = kakaoApi.getUserInfo(accessToken);

        String email = (String)userInfo.get("email");
        String nickname = (String)userInfo.get("nickname");


        System.out.println("email = " + email);
        System.out.println("nickname = " + nickname);
        System.out.println("accessToken = " + accessToken);
        // 3. DB에서 이메일로 사용자가 있는지 확인 (있으면 로그인, 없으면 회원가입)
        // 사용자가 없으면 회원가입
        Optional<User> user = authenticationService.kakaoSignup(email, nickname);

        // 사용자 정보가 존재하면 로그인 (JWT 발급)
        JwtAuthenticationResponse jwtAuthenticationResponse = authenticationService.kakaoSignin(user.get().getEmail());

        return ResponseEntity.ok(jwtAuthenticationResponse);
    }


    // 5. 최종적으로 클라이언트에 리디렉션 (NATIVE_APP_KEY를 사용해 kakao${NATIVE_APP_KEY}://oauth2 형태로 리디렉션)

//        String redirectUrl = "kakao" + nativeAppKey + "://oauth2?accessToken=" + jwtAuthenticationResponse.getAccessToken() + "&refreshToken=" + jwtAuthenticationResponse.getRefreshToken();
//
//        log.info(redirectUrl);
//        // 헤더에 리디렉션 URL을 담음
//        HttpHeaders headers = new HttpHeaders();
//        headers.setLocation(URI.create(redirectUrl));
//
//        // 302 응답을 반환하여 리디렉션
//        return new ResponseEntity<>(headers, HttpStatus.FOUND);

//    @GetMapping("/login/oauth2/{code}")
//    public ResponseEntity<String> loginByKakao(@RequestParam("code") String code) throws JsonProcessingException {
//        return ResponseEntity.ok("성공!!, code: " + code);
//    }
}