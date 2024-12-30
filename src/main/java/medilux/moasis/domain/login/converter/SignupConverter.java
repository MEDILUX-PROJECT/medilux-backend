package medilux.moasis.domain.login.converter;


import medilux.moasis.domain.login.domain.User;
import medilux.moasis.domain.login.dto.SignUpRequest;

public class SignupConverter {
    public static User toUser(SignUpRequest signUpRequest) {
        return User.builder()
                .nickname(signUpRequest.getNickname())
                .email(signUpRequest.getEmail())
                .password(signUpRequest.getPassword())
                .residence(signUpRequest.getResidence())
                .gender(signUpRequest.isGender())
                .phone(signUpRequest.getPhone())
                .build();
    }
}
