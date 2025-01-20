package medilux.moasis.domain.login.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import medilux.moasis.domain.login.domain.User;
import medilux.moasis.domain.login.dto.JwtAuthenticationResponse;
import medilux.moasis.domain.login.service.impl.AuthenticationServiceImpl;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    private final AuthenticationService authenticationService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        log.info("OAuth2 인증 요청 시작: clientRegistrationId={}", userRequest.getClientRegistration().getRegistrationId());

        // 리소스 서버에서 사용자 정보 가져오기
        OAuth2User oAuth2User;
        try {
            oAuth2User = super.loadUser(userRequest);
            log.info("OAuth2 사용자 정보 로드 성공: attributes={}", oAuth2User.getAttributes());
        } catch (OAuth2AuthenticationException e) {
            log.error("OAuth2 사용자 정보 로드 실패: error={}", e.getMessage(), e);
            throw e;
        }

        // 사용자 정보 추출
        String email = oAuth2User.getAttribute("email");
        String nickname = oAuth2User.getAttribute("name");
        log.info("사용자 정보 추출: 이메일={}, 닉네임={}", email, nickname);

        // 사용자 회원가입 또는 로그인 처리
        log.info("사용자 회원가입 또는 로그인 처리 시작");
        Optional<User> userOptional = authenticationService.googleSignup(email, nickname);
        User user = userOptional.orElseThrow(() -> {
            log.error("회원가입 실패: 이메일={}", email);
            return new IllegalStateException("회원가입 실패");
        });
        log.info("사용자 회원가입 또는 로그인 처리 완료: userId={}, email={}", user.getId(), user.getEmail());

        // 로그인 처리 및 JWT 생성
        log.info("JWT 생성 시작: email={}", email);
        JwtAuthenticationResponse jwtResponse = authenticationService.googleSignin(email);
        log.info("JWT 생성 완료: accessToken={}, refreshToken={}", jwtResponse.getAccessToken(), jwtResponse.getRefreshToken());

        // 반환할 OAuth2User를 사용자 정보와 함께 구성
        log.info("OAuth2User 구성 시작");
        DefaultOAuth2User defaultOAuth2User = new DefaultOAuth2User(
                Collections.singleton(new OAuth2UserAuthority(oAuth2User.getAttributes())),
                oAuth2User.getAttributes(),
                "email"
        );
        log.info("OAuth2User 구성 완료");

        return defaultOAuth2User;
    }
}
