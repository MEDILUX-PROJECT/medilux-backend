package medilux.moasis.domain.login.dto;

import lombok.Data;

@Data
public class JwtAuthenticationResponse {

    private String accessToken;

    private String refreshToken;
}
