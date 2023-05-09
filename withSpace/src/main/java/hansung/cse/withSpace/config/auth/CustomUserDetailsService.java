//package hansung.cse.withSpace.config.auth;
//
//import hansung.cse.withSpace.domain.Member;
//import hansung.cse.withSpace.exception.member.MemberNotFoundException;
//import hansung.cse.withSpace.repository.member.MemberRepository;
//import hansung.cse.withSpace.service.MemberService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//public class CustomUserDetailsService implements UserDetailsService {
//
//    private final MemberRepository memberRepository;
//
////    public CustomUserDetailsService(MemberRepository memberRepository) {
////        this.memberRepository = memberRepository;
////    }
//
//    @Override
//    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//        Member member = memberRepository.findByEmail(email)
//                .orElseThrow(() -> new UsernameNotFoundException("해당 사용자를 찾을 수 없습니다. email=" + email));
//
//        return new CustomUserDetails(
//                member.getId(), //pk
//                member.getEmail(),
//                member.getPassword(),
//                member.getMemberName(),
//                true,
//                null
//        );
//    }
//}
//
