package hansung.cse.withSpace.config.auth;

import hansung.cse.withSpace.domain.Member;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

@Getter
@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {
    private Long id; // 사용자의 Primary Key 값
    private String email;

    private String password;
    private String memberName;
    private final boolean enabled;

    private Collection<? extends GrantedAuthority> authorities;

//    public CustomUserDetails(Long id, String email, String password, String memberName, boolean enabled,
//                             Collection<? extends GrantedAuthority> authorities) {
//
//        this.enabled = enabled;
//        this.authorities = authorities;
//    }

    public CustomUserDetails(Long id, String email, String password, String memberName) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.memberName = memberName;
        this.enabled = true;
        this.authorities = Collections.singletonList(new SimpleGrantedAuthority("USER"));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() { //말이 getUsername이지 사용자의 아이디 << 여기선 이메일
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
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
}
