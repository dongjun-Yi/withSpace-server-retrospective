package hansung.cse.withSpace.config.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity // Spring Security 설정 활성화
@RequiredArgsConstructor
public class SecurityConfig{

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable().cors().disable()  //csrf와 cors disable

                .authorizeHttpRequests((requests) -> requests
                                //.requestMatchers("/status", "/", "/home", "/signup", "/member").permitAll()
                        .anyRequest().permitAll() //임시로 모든 페이지 접근 허용
                        //로그인 안 해도 위 url들은 접근 가능
                                //.anyRequest().authenticated() // 어떠한 요청이라도 인증이 필요
                )
                .formLogin((form) -> form // form 방식 로그인 사용
                        .loginPage("/login")  //로그인 페이지
                        .loginProcessingUrl("/login-process") //submit을 받을 url - 시큐리티가 처리해줌(MyMemberDetailService로 넘겨준것을)
                        .usernameParameter("email") //submit할 아이디(이메일)
                        .passwordParameter("password") //submit할 비밀번호
                        .defaultSuccessUrl("/main", true) //성공시
                        .failureUrl("/login") //로그인 실패시 다시 로그인화면
                        .permitAll()  // 로그인 페이지 이동이 막히면 안되므로 관련된애들 모두 허용
                )
                .logout(withDefaults());  // 로그아웃은 기본설정으로 (/logout으로 인증해제)
        //.logout((logout) -> logout.permitAll());

        return http.build();
    }

}
