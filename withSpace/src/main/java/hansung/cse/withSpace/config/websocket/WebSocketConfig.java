package hansung.cse.withSpace.config.websocket;

import hansung.cse.withSpace.config.auth.CustomUserDetails;
import hansung.cse.withSpace.config.auth.MyMemberDetailService;
import hansung.cse.withSpace.config.jwt.JwtAuthenticationFilter;
import hansung.cse.withSpace.config.jwt.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

import java.util.List;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {


    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtTokenUtil jwtTokenUtil;

    private final MyMemberDetailService myMemberDetailService;
    private final StompHandler stompHandler;



    final Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // 메시지를 발행할 토픽 주소 설정
        // 메시지 구독 요청 url -> 메시지 받을 때
        config.enableSimpleBroker("/topic");

        // 메시지 발행 요청 url -> 메시지 보낼 때
        // 클라이언트에서 메시지를 보낼 주소 prefix 설정
        config.setApplicationDestinationPrefixes("/app");
    }



//    @Override
//    public void configureClientInboundChannel(ChannelRegistration registration) {
//
//        registration.interceptors(new ChannelInterceptor() {
//            @Override
//            public Message<?> preSend(Message<?> message, MessageChannel channel) {
//
//                StompHeaderAccessor accessor =
//                        MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
//                if (StompCommand.CONNECT.equals(accessor.getCommand())) {
//                    List<String> authorization = accessor.getNativeHeader("JWT-Authorization");
//                    if (authorization != null && authorization.size() > 0) {
//                        String authToken = authorization.get(0).substring(7);
//                        String username = jwtTokenUtil.getUsernameFromToken(authToken);
//
//                        if (username != null && jwtTokenUtil.validateToken(authToken)) {
//
//                            UserDetails userDetails = myMemberDetailService.getUserDetailsByEmail(username);
//
//                            UsernamePasswordAuthenticationToken user =
//                                    new UsernamePasswordAuthenticationToken(
//                                            userDetails, null, userDetails.getAuthorities());
//                            accessor.setUser(user);
//                        }
//                        else{
//                            log.error(authToken);
//                        }
//                    }
//                }
//                return message;
//            }
//        });
//    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration){
        registration.interceptors(stompHandler);
    }


    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/chat")
                .setAllowedOrigins("*");

        registry.addEndpoint("/chat")
                .setAllowedOrigins("*")
                .withSockJS();

        registry.addEndpoint("/chat").
                setAllowedOrigins("http://localhost:3000").
                withSockJS();

        registry.addEndpoint("/chat").
                setAllowedOrigins("http://localhost:3000");
    }


}
