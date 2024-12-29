package medilux.moasis.domain.login.service;

import medilux.moasis.domain.login.domain.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;

public interface JwtService {

    String extractUserEmail(String token);

    Long extractUserId(String token);

    String generateToken(User user);

    boolean isTokenValid(String token, UserDetails userDetails);

    String generateRefreshToken(Map<String, Object> extraClaims, UserDetails userDetails);
}
