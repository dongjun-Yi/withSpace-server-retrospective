package hansung.cse.withSpace.config.auth;

import hansung.cse.withSpace.config.jwt.JwtAuthenticationSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity // Spring Security 설정 활성화
@RequiredArgsConstructor
//@EnableGlobalAuthentication
@EnableGlobalMethodSecurity(prePostEnabled = true) //@PreAuthorize @PostAuthorize 활성화
public class SecurityConfig{

    @Autowired
    MyMemberDetailService myMemberDetailService;

    @Autowired
    private GoogleOauth2UserService googleOauth2UserService;

    @Autowired
    private OAuth2AuthorizedClientService oath2;
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private JwtAuthenticationSuccessHandler jwtAuthenticationSuccessHandler;


//    @Autowired
//    private JwtTokenProvider jwtTokenProvider;



//    @Bean
//    public JwtTokenProvider jwtTokenProvider() {
//        return new JwtTokenProvider();
//    }

//    @Autowired
//    private DataSource dataSource;



//    @Bean
//    public SessionAuthenticationStrategy sessionAuthenticationStrategy() {
//        return new SessionFixationProtectionStrategy();
//    }
//
//    private class SessionIdFilter extends OncePerRequestFilter {
//        @Override
//        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//                throws ServletException, IOException {
//            HttpSession session = request.getSession();
//            if (session != null) {
//                String sessionId = session.getId();
//                Cookie cookie = new Cookie("JSESSIONID", sessionId);
//                cookie.setPath(request.getContextPath());
//                response.addCookie(cookie);
//            }
//            filterChain.doFilter(request, response);
//        }
//    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable().cors().disable()  //csrf와 cors disable

//                .authorizeHttpRequests((requests) -> requests
//                                //.requestMatchers("/status", "/", "/home", "/signup", "/member").permitAll()
//                                .anyRequest().permitAll() //임시로 모든 페이지 접근 허용
//                        //로그인 안 해도 위 url들은 접근 가능
//                                //.anyRequest().authenticated() // 어떠한 요청이라도 인증이 필요
//                )

                .authorizeHttpRequests(requests -> requests //로그인 전
                        .requestMatchers("/", "/home", "/signup", "/member", "/login","/oauth2/**", "/profile").permitAll()
                        //.requestMatchers("/main").hasRole("USER") //로그인후에는 /main페이지 허용
                        //.anyRequest().permitAll() //임시로 모든 페이지 접근 허용
                        //로그인 안 해도 위 url들은 접근 가능
                        .anyRequest().authenticated() // 어떠한 요청이라도 인증이 필요
                )

                //일반 로그인
                .formLogin((form) -> form // form 방식 로그인 사용
                        .loginPage("/login")  //로그인 페이지
                        .loginProcessingUrl("/login-process") //submit을 받을 url - 시큐리티가 처리해줌(MyMemberDetailService로 넘겨준것을)
                        .usernameParameter("email") //submit할 아이디(이메일)
                        .passwordParameter("password") //submit할 비밀번호
                        //.defaultSuccessUrl("/login", true) //성공시
                        .failureUrl("/login") //로그인 실패시 다시 로그인화면
                        .permitAll()  // 로그인 페이지 이동이 막히면 안되므로 관련된애들 모두 허용
                        .successHandler(jwtAuthenticationSuccessHandler)

                )


                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/oauth2/authorization/google")
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(googleOauth2UserService)
                        )
                        .defaultSuccessUrl("/main", true) //성공시
                        .permitAll()
                )


                .logout(logout->logout.logoutSuccessUrl("/")) // 로그아웃시 /로 이동
                //.exceptionHandling(e-> e.accessDeniedPage("/access-denied")) //접근 권한이 없는 페이지에 대한 예외 처리

                .rememberMe() // Remember-Me 기능 활성화
                .key("mySecretKey")
                .rememberMeCookieName("my-remember-me-cookie")
                .tokenValiditySeconds(86400*7) //1일 * 7 = 7일동안 로그인 유지
                .userDetailsService(myMemberDetailService)

                .and()
                .httpBasic()//postman 사용시 필요


//                .and()
//                .sessionManagement()
//                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
//                .sessionFixation().migrateSession()
//                .sessionAuthenticationStrategy(sessionAuthenticationStrategy())
//                .maximumSessions(1)
//                .maxSessionsPreventsLogin(false)
//                .expiredUrl("/login?expired")

        ;



        //.logout((logout) -> logout.permitAll());
        //http.addFilterBefore(new SessionIdFilter(), BasicAuthenticationFilter.class); // SessionIdFilter 등록

        return http.build();
    }

}
