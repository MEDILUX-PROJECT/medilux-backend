package medilux.moasis.domain.login.dto;

import lombok.Data;

@Data
public class SignUpRequest {

    private String nickname;

    private String email;

    private String password;

    private String residence;

    private boolean gender;

    private String phone;

    private boolean sns;
}
