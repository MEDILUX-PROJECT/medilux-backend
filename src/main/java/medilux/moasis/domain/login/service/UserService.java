package medilux.moasis.domain.login.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Map;

public interface UserService {

    UserDetailsService userDetailsService();

    Map<String, Object> getProfileInfo(Long loginId);
}

