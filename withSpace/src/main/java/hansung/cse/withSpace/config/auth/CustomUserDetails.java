package hansung.cse.withSpace.config.auth;

import hansung.cse.withSpace.domain.Member;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Getter
@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {
    private Long id; // 사용자의 Primary Key 값
    private UUID uuid;
    private String email;

    private String password;
    private String memberName;
    private final boolean enabled;

    private Collection<? extends GrantedAuthority> authorities;


    public CustomUserDetails(Long id, UUID uuid, String email, String password, String memberName) {
        this.id = id;
        this.uuid = uuid;
        this.email = email;
        this.password = password;
        this.memberName = memberName;
        this.enabled = true;

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        this.authorities = authorities;
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
