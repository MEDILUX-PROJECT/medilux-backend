package medilux.moasis.domain.login.service.impl;

import lombok.RequiredArgsConstructor;
import medilux.moasis.domain.login.domain.User;
import medilux.moasis.domain.login.repository.UserRepository;
import medilux.moasis.domain.login.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String userEmail) {
                return userRepository.findByEmail(userEmail)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            }
        };
    }

    @Override
    public Map<String, Object> getProfileInfo(Long loginId) {
        return null;
//        return getStringObjectMap(loginId);
    }

//    private Map<String, Object> getStringObjectMap(Long visitedUserId) {
//        Optional<com.example.domain.User> optionalUser = userRepository.findById(visitedUserId);
//
//        if (optionalUser.isPresent()) {
//            User user = optionalUser.get();
//
//            Map<String, Object> profileInfo = new HashMap<>();
////            profileInfo.put("userId", user.getId());
//            profileInfo.put("nickname", user.getNickname());
////            profileInfo.put("password", user.getPassword());
//            profileInfo.put("Email", user.getEmail());
//            profileInfo.put("Gender", user.isGender());
//            profileInfo.put("Role", user.getRole());
//            profileInfo.put("ProfileImg", user.getProfileImageUrl());
//
//            return profileInfo;
//        } else {
//            return Collections.emptyMap();
//        }
//    }

    public User getUser(Long userId){
        User user = userRepository.findById(userId).orElseThrow();
        return user;
    }
}
