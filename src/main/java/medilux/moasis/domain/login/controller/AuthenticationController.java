//package medilux.moasis.domain.login.controller;
//
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import medilux.moasis.domain.login.dto.*;
//import medilux.moasis.domain.login.service.AuthenticationService;
//import medilux.moasis.global.exception.BaseResponse;
//import medilux.moasis.global.exception.BaseResponseService;
//import medilux.moasis.global.exception.CustomExceptionHandler;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
//import org.springframework.web.bind.annotation.*;
//
//@Slf4j
//@RestController
//@RequestMapping("/api/login/auth")
//@RequiredArgsConstructor
//public class AuthenticationController {
//
//    private final AuthenticationService authenticationService;
//
//    private final BaseResponseService baseResponseService;
//
//    private final CustomExceptionHandler customExceptionHandler;
//
//    // 회원가입 - (1) 아이디 중복 확인
//    @GetMapping("signup/{loginEmail}")
//    public BaseResponse<Object> checkLoginIdDuplicate(@PathVariable(name = "loginEmail") String loginEmail) {
//        if (authenticationService.checkLoginIdDuplicate(loginEmail)) {
//            return BaseResponse.<Object>builder()
//                    .code(2100)
//                    .isSuccess(false)
//                    .message("아이디가 이미 존재합니다.")
//                    .build();
//        } else {
//            return BaseResponse.<Object>builder()
//                    .code(2000)
//                    .isSuccess(true)
//                    .message("해당 아이디를 사용할 수 있습니다.")
//                    .build();
//        }
//    }
//
//    // 회원가입 - (2) 비밀번호 조건 확인
//    @PostMapping("/signup/passwordCheck")
//    public BaseResponse<Object> passwordCheck(@RequestBody PasswordCheckRequest passwordCheckRequest) {
//        // 제약 검증
//        if (!passwordCheckRequest.getFirstPassword().matches("(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}")) {
//            // 제약 조건을 만족하지 않는 경우
//            return BaseResponse.<Object>builder()
//                    .code(2102)
//                    .isSuccess(false)
//                    .message("영어(대/소문), 숫자, 특수문자를 포함해주세요.")
//                    .build();
//        }
//
//        if (authenticationService.passwordCheck(passwordCheckRequest.getFirstPassword(), passwordCheckRequest.getSecondPassword())) {
//            return BaseResponse.<Object>builder()
//                    .code(2101)
//                    .isSuccess(true)
//                    .message("비밀번호가 확인되었습니다.")
//                    .build();
//        } else {
//            return BaseResponse.<Object>builder()
//                    .code(2001)
//                    .isSuccess(false)
//                    .message("비밀번호가 일치하지 않습니다.")
//                    .build();
//        }
//    }
//
//    /**
//     *  회원가입 - (3) 이메일 인증 확인 (MailController에서 진행)
//     *  회원가입 - (4) 유저가 받은 인증번호 일치 확인 (MailController에서 진행)
//     */
//
//    // 회원가입 - (5) 최종 회원가입 완료
//    @PostMapping("/signup")
//    public BaseResponse<Object> signup(@RequestBody SignUpRequest signUpRequest) {
//        authenticationService.signup(signUpRequest);
//        return BaseResponse.<Object>builder()
//                .code(2004)
//                .isSuccess(true)
//                .message("회원가입이 완료되었습니다.")
//                .build();
//    }
//
//
//    @PostMapping("/signin")
//    public ResponseEntity<JwtAuthenticationResponse> signin(@RequestBody SignInRequest signinRequest) {
//
//        return ResponseEntity.ok(authenticationService.signin(signinRequest));
//
//    }
//
//    @PostMapping("/kakao/signin")
//    public ResponseEntity<JwtAuthenticationResponse> kakaoSignin(@RequestBody String email) {
//        return ResponseEntity.ok(authenticationService.kakaoSignin(email));
//
//    }
//
//
//    @PostMapping("/refresh")
//    public ResponseEntity<JwtAuthenticationResponse> refresh(@RequestBody RefreshTokenRequest refreshTokenRequest) {
//        return ResponseEntity.ok(authenticationService.refreshToken(refreshTokenRequest));
//
//    }
//
//    // JWT 토큰 로그아웃
//    @PostMapping("/logout")
//    public BaseResponse<Object> logout(HttpServletRequest request, HttpServletResponse response) {
//        try {
//            // Spring Security의 로그아웃 핸들러를 사용하여 로그아웃 처리
//            SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
//            logoutHandler.logout(request, response, SecurityContextHolder.getContext().getAuthentication());
//
//            return BaseResponse.<Object>builder()
//                    .code(2000)
//                    .isSuccess(true)
//                    .message("로그아웃이 완료되었습니다.")
//                    .build();
//        } catch (Exception e) {
//            log.error("로그아웃 처리 중 오류 발생: {}", e.getMessage());
//            return BaseResponse.<Object>builder()
//                    .code(5000)
//                    .isSuccess(false)
//                    .message("로그아웃 처리 중 오류가 발생했습니다.")
//                    .build();
//        }
//    }
//
//
//}
