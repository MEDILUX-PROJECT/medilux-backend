//package medilux.moasis.domain.login.controller;
//
//import jakarta.servlet.http.HttpServletRequest;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import medilux.moasis.domain.login.dto.ProfileEditRequest;
//import medilux.moasis.domain.login.service.AuthenticationService;
//import medilux.moasis.domain.login.service.JwtService;
//import medilux.moasis.domain.login.service.UserService;
//import medilux.moasis.global.exception.BaseResponse;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.util.Map;
//
//@Slf4j
//@RestController
//@RequestMapping("/api/login/profile")
//@RequiredArgsConstructor
//public class UserController {
//    private final UserService userService;
////    private final FileUploadService fileUploadService;
//    private final JwtService jwtService;
//    private final AuthenticationService authenticationService;
//
//    @GetMapping
//    public BaseResponse<Object> profile(@RequestHeader("Authorization") String accessToken) {
//        log.info("start!!!");
//        String token = accessToken.substring(7);
//
//        Long loginId = jwtService.extractUserId(token);
//        log.info("loginId: "+loginId);
//
//        Map<String, Object> profileInfo = userService.getProfileInfo(loginId);
//        log.info("profileInfo: "+profileInfo.size());
//        for (Map.Entry<String, Object> entry : profileInfo.entrySet()) {
//            System.out.println(entry.getKey() + ": " + entry.getValue());
//        }
//        return BaseResponse.<Object>builder()
//                .code(200)
//                .isSuccess(true)
//                .data(profileInfo)
//                .build();
//    }
//
//    // 회원 탈퇴
//    @DeleteMapping("/deleteUser")
//    public BaseResponse<Object> withdrawUser(@RequestHeader(name="Authorization") String accessToken, HttpServletRequest request) {
//        try {
//            String token = accessToken.substring(7);
//            Long loginId = jwtService.extractUserId(token);
//            // 사용자 ID를 기반으로 회원 탈퇴 처리
//            authenticationService.deleteUserById(loginId);
//            return BaseResponse.<Object>builder()
//                    .code(200)
//                    .isSuccess(true)
//                    .message("회원 탈퇴가 완료되었습니다.")
//                    .build();
//        } catch (Exception e) {
//            log.error("회원 탈퇴 처리 중 오류 발생: {}", e.getMessage());
//            return BaseResponse.<Object>builder()
//                    .code(5000)
//                    .isSuccess(false)
//                    .message("회원 탈퇴 처리 중 오류가 발생했습니다.")
//                    .build();
//        }
//    }
//}
