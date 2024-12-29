package medilux.moasis.domain.login.dto;

import lombok.Data;

@Data
public class ProfileRequest {
    private String nickname;        // 필수

    private String email;

    private String password;

    private String residence;

    private boolean gender;

    private String phone;

    private boolean sns;
}
