package medilux.moasis.domain.login.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.AuthProvider;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Builder
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "user")
public class User implements UserDetails {

    public String getNickname() {
        return nickname;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getPhone() {
        return phone;
    }

    public String getResidence() {
        return residence;
    }

    public boolean isGender() {
        return gender;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public LocalDateTime getUpdateAt() {
        return updateAt;
    }

    @jakarta.persistence.Id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;                     // PK

    @Column(length = 50, nullable = false)
    private String nickname;            // 닉네임

    @Column(name = "e_mail", length = 100, nullable = false, unique = true)
    private String email;               // 이메일

    @Column(length = 100)
    private String password;            // 비밀번호 (소셜 로그인만 사용시 nullable 가능)

    @Column(length = 20)
    private String phone;               // 휴대전화

    private String residence;           // 거주지 (ERD에는 없지만 기존 코드에 있어서 유지)

    private boolean gender;             // 성별 (ERD에는 없지만 기존 코드에 있어서 유지)

    @Column(name = "create_at")
    private LocalDateTime createAt;     // 가입일

    @Column(name = "update_at")
    private LocalDateTime updateAt;     // 수정일

//    // 양방향 매핑을 원할 경우
//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<AuthProvider> authProviders;

    // == setter 유사 메서드들 ==
    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public void setResidence(String residence) {
        this.residence = residence;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * UserDetails 구현체 부분
     * - 소셜 로그인만 사용한다면 권한이나 계정 만료 등의 로직을 간단히 처리 가능합니다.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 권한 로직이 필요하다면 List<GrantedAuthority>로 반환
        return Collections.emptyList();
    }

    @Override
    public String getUsername() {
        return this.email; // 보통 email을 userName으로 사용
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // 추가 로직이 필요하다면 false 처리
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
