package medilux.moasis.domain.login.dto;

import lombok.Data;

@Data
public class ProfileEditRequest {
    private String nickname;

    private String residence;

    private boolean gender;

    private String phone;
}
