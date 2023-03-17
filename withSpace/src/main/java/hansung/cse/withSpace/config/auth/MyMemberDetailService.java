package hansung.cse.withSpace.config.auth;

import hansung.cse.withSpace.service.MemberService;
import hansung.cse.withSpace.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MyMemberDetailService implements UserDetailsService {

    private final MemberService memberService;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Member> findOne = memberService.findByEmail(email);
        Member member = findOne.orElseThrow(() -> new UsernameNotFoundException("없는 회원입니다"));

        return User.builder()
                .username(member.getEmail())
                .password(member.getPassword())
                .roles("USER")
                .build();
    }

}
