package hansung.cse.withSpace.config.websocket;

import hansung.cse.withSpace.config.jwt.JwtAuthenticationFilter;
import hansung.cse.withSpace.config.jwt.JwtTokenUtil;
import hansung.cse.withSpace.domain.Member;
import hansung.cse.withSpace.exception.jwt.TokenInvalidateException;
import hansung.cse.withSpace.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandlingException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class StompHandler implements ChannelInterceptor {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final JwtTokenUtil jwtTokenUtil;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final MemberService memberService;
    //private final RedisTemplate<String, Object> redisTemplate;
    private final StringRedisTemplate stringRedisTemplate;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        log.info("Stomp before handshake ");

        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        log.info("message:" + message);
        log.info("헤더 : " + message.getHeaders());
        log.info("토큰 = " + accessor.getFirstNativeHeader("Authorization"));

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {

            String token = Objects.requireNonNull(accessor.getFirstNativeHeader("Authorization"))
                    .substring(7);

            UUID uuid = jwtAuthenticationFilter.getUUIDFromToken(token);
            Long id = memberService.findByUuid(uuid).getId();
            memberService.setMemberActive(id);
            String memberId = String.valueOf(id);

            //세션 정보 Redis에 저장
            String memberIdKey = "memberId:"+ memberId;
            stringRedisTemplate.opsForValue().set(memberIdKey, memberId, 5, TimeUnit.SECONDS);


            log.info("웹소켓 연결 redis 저장");
            log.info("memberId = " + memberId);
            log.info("LocalDateTime.now() = " + LocalDateTime.now());

        }

        return message;

    }




}