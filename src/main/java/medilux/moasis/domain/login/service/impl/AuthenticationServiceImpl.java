package medilux.moasis.domain.login.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import medilux.moasis.domain.login.domain.User;
import medilux.moasis.domain.login.dto.JwtAuthenticationResponse;
import medilux.moasis.domain.login.dto.RefreshTokenRequest;
import medilux.moasis.domain.login.dto.SignInRequest;
import medilux.moasis.domain.login.dto.SignUpRequest;
import medilux.moasis.domain.login.repository.UserRepository;
import medilux.moasis.domain.login.service.AuthenticationService;
import medilux.moasis.domain.login.service.JwtService;
import medilux.moasis.domain.login.service.RandomPasswordGenerator;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Optional;



@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {


    private final UserRepository userRepository;

//    private final PasswordEncoder passwordEncoder;

//    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    public User signup(SignUpRequest signUpRequest) {

        //ser user = SignupConverter.toUser(signUpRequest, Role.ROLE_USER);


        // 비밀번호 암호화
//        String encodedPassword = passwordEncoder.encode(signUpRequest.getPassword());

        // User 객체 생성
        User user = User.builder()
                .nickname(signUpRequest.getNickname())
                .email(signUpRequest.getEmail())
//                .password(encodedPassword) // 암호화된 비밀번호 설정
                //.password(signUpRequest.getPassword())
                .residence(signUpRequest.getResidence())
                .gender(signUpRequest.isGender())
                .phone(signUpRequest.getPhone())
                .build();

        // 사용자 저장
        return userRepository.save(user);
    }

    @Override
    public boolean kakaoIdExists(String kakaoEmail) {
        return userRepository.existsByEmail(kakaoEmail);
    }

    @Override
    public Optional<User> kakaoSignup(String email, String nickname) { // 카카오 회원가입

        if (userRepository.existsByEmail(email)) {
            return userRepository.findByEmail(email);
        }
        User user = User.builder()
                .email(email)
                .nickname(nickname)
//                .password(passwordEncoder.encode(RandomPasswordGenerator.generateRandomPassword()))
                .residence(null)
                .gender(true)
                .phone(null)
                .build();

        return Optional.of(userRepository.save(user));

    }

    @Override
    public Optional<User> googleSignup(String email, String nickname) {
        // 이메일로 이미 사용자가 존재하는지 확인
        if (userRepository.existsByEmail(email)) {
            log.info("기존 회원");
            return userRepository.findByEmail(email); // 이미 존재하면 해당 사용자 반환
        }

        // 새로운 사용자 생성
        User user = User.builder()
                .email(email)
                .nickname(nickname)
                .residence(null) // Google 로그인 시 기본 값 설정
                .gender(true) // 기본 값 설정 (필요에 따라 수정)
                .phone(null) // 기본 값 설정
                .build();

        // 저장 후 반환
        return Optional.of(userRepository.save(user));
    }


    public JwtAuthenticationResponse signin(SignInRequest signinRequest) {

//        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
//                signinRequest.getEmail(), signinRequest.getPassword()));

        var user = userRepository.findByEmail(signinRequest.getEmail()).orElseThrow(() -> new IllegalArgumentException("Invalid login_Id or password"));
        var jwt = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(new HashMap<>(), user);

        JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();

        jwtAuthenticationResponse.setAccessToken(jwt);
        jwtAuthenticationResponse.setRefreshToken(refreshToken);
        return jwtAuthenticationResponse;

    }

    public JwtAuthenticationResponse kakaoSignin (String email) {
        var user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("Invalid login_Id"));
        var jwt = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(new HashMap<>(), user);

        JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();

        jwtAuthenticationResponse.setAccessToken(jwt);
        jwtAuthenticationResponse.setRefreshToken(refreshToken);
        return jwtAuthenticationResponse;

    }


    @Override
    public JwtAuthenticationResponse googleSignin(String email) {
        // 이메일로 사용자 찾기
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Invalid email"));

        // JWT 액세스 토큰 및 리프레시 토큰 생성
        var jwt = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(new HashMap<>(), user);

        // JWT 응답 생성
        JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();
        jwtAuthenticationResponse.setAccessToken(jwt);
        jwtAuthenticationResponse.setRefreshToken(refreshToken);

        return jwtAuthenticationResponse;
    }


    public JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        String userLoginId = jwtService.extractUserEmail(refreshTokenRequest.getToken());
        User user = userRepository.findByEmail(userLoginId).orElseThrow();

        if (jwtService.isTokenValid(refreshTokenRequest.getToken(), user)) {
            var jwt = jwtService.generateToken(user);

            JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();

            jwtAuthenticationResponse.setAccessToken(jwt);
            jwtAuthenticationResponse.setRefreshToken(refreshTokenRequest.getToken());
            return jwtAuthenticationResponse;
        }
        return null;
    }

    public Boolean checkLoginIdDuplicate(String loginEmail) {
        return userRepository.existsByEmail(loginEmail);
    }


    public boolean passwordCheck(String firstpassword, String secondpassword) {
        return firstpassword.equals(secondpassword);
    }

    public void deleteUserById(Long userId) {
        // userId를 통해 해당 유저가 존재하는지 확인하고 삭제
        userRepository.findById(userId)
                .ifPresentOrElse(
                        user -> userRepository.delete(user),
                        () -> {
                            throw new IllegalArgumentException("해당 사용자가 존재하지 않습니다.");
                        });
    }
}