package medilux.moasis.domain.login.service;


import medilux.moasis.domain.login.domain.User;
import medilux.moasis.domain.login.dto.JwtAuthenticationResponse;
import medilux.moasis.domain.login.dto.RefreshTokenRequest;
import medilux.moasis.domain.login.dto.SignInRequest;
import medilux.moasis.domain.login.dto.SignUpRequest;

import java.util.Optional;


public interface AuthenticationService {

    User signup(SignUpRequest signUpRequest);

    boolean kakaoIdExists(String kakaoEmail);

    Optional<User> kakaoSignup(String email, String nickname);

    JwtAuthenticationResponse signin(SignInRequest signinRequest);

    JwtAuthenticationResponse kakaoSignin(String email);


    JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest);

    Boolean checkLoginIdDuplicate(String loginEmail);

    boolean passwordCheck(String pw1, String pw2);

    void deleteUserById(Long userId);

}
